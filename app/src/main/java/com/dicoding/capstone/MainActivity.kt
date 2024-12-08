package com.dicoding.capstone

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Initialize ActivityResultLaunchers
        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("MainActivity", "Photo URI: $photoUri")
                // Pindah ke ResultFragment dan kirim URI gambar
                navigateToResultFragment(photoUri)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

        galleryResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Log.d("MainActivity", "Photo URI received from gallery: $uri")
                // Pindah ke ResultFragment dan kirim URI gambar
                navigateToResultFragment(it)
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

    // Open camera
    private fun openCamera() {
        // Buat file gambar di penyimpanan eksternal
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile
        )

        // Intent untuk mengambil foto dengan kamera
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri) // Menyimpan gambar ke URI yang ditentukan
        }

        cameraResultLauncher.launch(cameraIntent)
    }

    // Open gallery
    private fun openGallery() {
        galleryResultLauncher.launch("image/*")
    }

    // Handle image from URI
    private fun navigateToResultFragment(imageUri: Uri) {
        // Pindah ke ResultFragment dan kirim URI gambar
        val resultFragment = ResultFragment.newInstance(imageUri)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, resultFragment)
            .addToBackStack(null)  // Agar bisa kembali ke fragment sebelumnya
            .commit()
    }

    // Create image file in external storage
    private fun createImageFile(): File {
        // Buat file di penyimpanan eksternal untuk gambar
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: filesDir
        return File.createTempFile(
            "JPEG_${timestamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    // Handle camera permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Show bottom navigation when navigating back to Home or other fragments
    override fun onBackPressed() {
        super.onBackPressed()
        // Show bottom navigation again when going back from ResultFragment
        bottomNavigationView.visibility = View.VISIBLE
    }
}
