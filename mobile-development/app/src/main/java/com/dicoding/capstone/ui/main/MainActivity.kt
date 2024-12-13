package com.dicoding.capstone.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.dicoding.capstone.ui.home.HomeFragment
import com.dicoding.capstone.R
import com.dicoding.capstone.data.api.ApiService
import com.dicoding.capstone.data.api.RetrofitClient
import com.dicoding.capstone.ui.history.HistoryFragment
import com.dicoding.capstone.ui.maps.HospitalFragment
import com.dicoding.capstone.ui.profile.ProfileFragment
import com.dicoding.capstone.ui.result.ResultFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        progressBar = findViewById(R.id.progressBar2) // Tambahkan ProgressBar
        progressBar.visibility = View.GONE // Sembunyikan ProgressBar di awal

        // Initialize ActivityResultLaunchers
        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("MainActivity", "Photo URI: $photoUri")
                // Upload image to API
                uploadImage(photoUri)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

        galleryResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Log.d("MainActivity", "Photo URI received from gallery: $uri")
                // Upload image to API
                uploadImage(it)
            } ?: run {
                Toast.makeText(this, "Error: No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val selectedFragment: Fragment = HomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment, HomeFragment::class.java.simpleName)
                        .commit()
                }

                R.id.navigation_history -> {
                    val selectedFragment: Fragment = HistoryFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }

                R.id.navigation_dashboard -> {
                    // Show dialog to choose camera or gallery
                    showCameraOrGalleryDialog()
                }

                R.id.navigation_notifications -> {
                    val selectedFragment: Fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit()
                }

                R.id.navigation_hospital -> {
                    val selectedFragment: Fragment = HospitalFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment, HospitalFragment::class.java.simpleName)
                        .commit()
                }
            }
            true
        }

        // Set OnClickListener for FloatingActionButton
        floatingActionButton.setOnClickListener {
            // Tampilkan dialog untuk memilih kamera atau galeri
            showCameraOrGalleryDialog()
        }
    }

    // Show dialog to choose camera or gallery
    private fun showCameraOrGalleryDialog() {
        val options = arrayOf("Camera", "Gallery")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkCameraPermissionAndOpen() // Camera
                1 -> openGallery() // Gallery
            }
        }
        builder.show()
    }

    // Check and request camera permission
    private fun checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1001)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        AlertDialog.Builder(this)
            .setTitle("Note")
            .setMessage("Please upload a photo of one side of the eye only")
            .setPositiveButton("OK") { _, _ ->
                val photoFile = createImageFile()
                photoUri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.provider",
                    photoFile
                )

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                }

                cameraResultLauncher.launch(cameraIntent)
            }
            .show()
    }

    private fun openGallery() {
        AlertDialog.Builder(this)
            .setTitle("Note")
            .setMessage("Please upload a photo of one side of the eye only")
            .setPositiveButton("OK") { _, _ ->
                galleryResultLauncher.launch("image/*")
            }
            .show()
    }

    private fun uploadImage(imageUri: Uri) {
        progressBar.visibility = View.VISIBLE // Tampilkan ProgressBar sebelum proses upload
        CoroutineScope(Dispatchers.IO).launch {
            val inputStream = contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE // Sembunyikan ProgressBar jika gagal
                    Toast.makeText(
                        this@MainActivity,
                        "Error: Unable to open input stream from URI",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            val file = File.createTempFile("temp_image", ".jpg", cacheDir)
            val outputStream = FileOutputStream(file)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = file.asRequestBody("image/*".toMediaType())
            val body = MultipartBody.Part.createFormData("files", file.name, requestFile)

            val apiService = RetrofitClient.instance.create(ApiService::class.java)
            val response =
                apiService.uploadImage(body).execute() // Use execute() for synchronous call

            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE // Sembunyikan ProgressBar setelah selesai
                if (response.isSuccessful) {
                    val result = response.body()?.results?.get(0)
                    // Pastikan prediction_score tetap String
                    val predictionScoreString = result?.prediction_score
                    navigateToResultFragment(imageUri, result?.condition, predictionScoreString)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Upload failed: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigateToResultFragment(imageUri: Uri?, condition: String?, predictionScore: String?) {
        // Pastikan parameter tidak null sebelum membuat fragment
        if (imageUri != null && condition != null && predictionScore != null) {
            val resultFragment = ResultFragment.newInstance(imageUri, condition, predictionScore)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .addToBackStack(null)
                .commit()
        } else {
            // Tangani jika ada parameter yang null, misalnya tampilkan pesan error atau log
            Log.e("Navigate", "Invalid data provided to ResultFragment.")
        }
    }


    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: filesDir
        return File.createTempFile(
            "JPEG_${timestamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bottomNavigationView.visibility = View.VISIBLE
    }
}