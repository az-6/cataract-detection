package com.dicoding.capstone

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.dicoding.capstone.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    // Inisialisasi ViewBinding
    private lateinit var binding: ActivitySignupBinding

    // Inisialisasi FirebaseAuth
    private lateinit var mAuth: FirebaseAuth

    // Inisialisasi Firestore
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivitySignupBinding.inflate(layoutInflater)

        // Set content view dengan binding.root
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth dan Firestore
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Mengatur aksi untuk tombol signup
        binding.btnSignup.setOnClickListener {
            handleSignup()
        }

        // Mengatur validasi untuk input email dan password
        setupValidation()
        playAnimation()

        // Menambahkan aksi ketika klik pada "Login Now"
        binding.txtLoginNow.setOnClickListener {
            // Intent untuk berpindah ke halaman Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk mengatur validasi form
    private fun setupValidation() {
        // Validasi email
        binding.edtEmail.doOnTextChanged { text, _, _, _ ->
            binding.edtEmail.error = when {
                text.isNullOrEmpty() -> "Email cannot be empty"
                !text.contains("@") -> "Email format is invalid"
                else -> null
            }
        }

        // Validasi password
        binding.edtPass.doOnTextChanged { text, _, _, _ ->
            binding.edtPass.error = when {
                text.isNullOrEmpty() -> "Password cannot be empty"
                text.length < 6 -> "Password must be at least 6 characters"
                else -> null
            }
        }

        // Validasi nama
        binding.edtName.doOnTextChanged { text, _, _, _ ->
            binding.edtName.error = when {
                text.isNullOrEmpty() -> "Name cannot be empty"
                else -> null
            }
        }
    }

    // Fungsi untuk menangani proses signup
    private fun handleSignup() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPass.text.toString().trim()

        // Validasi form saat tombol signup diklik
        if (name.isEmpty()) {
            binding.edtName.error = "Name is required"
            return
        }
        if (email.isEmpty()) {
            binding.edtEmail.error = "Email is required"
            return
        }
        if (password.isEmpty()) {
            binding.edtPass.error = "Password is required"
            return
        }

        // Validasi tambahan untuk email dan password
        if (!email.contains("@")) {
            binding.edtEmail.error = "Invalid email format"
            return
        }
        if (password.length < 6) {
            binding.edtPass.error = "Password must be at least 6 characters"
            return
        }

        // Menampilkan progress bar selama signup
        showLoading(true)

        // Melakukan registrasi dengan Firebase
        performFirebaseSignup(name, email, password)
    }

    // Fungsi untuk melakukan signup menggunakan Firebase Authentication
    private fun performFirebaseSignup(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)

                if (task.isSuccessful) {
                    val user = mAuth.currentUser

                    // Menyimpan data pengguna ke Firestore
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email
                    )

                    user?.let {
                        firestore.collection("users").document(it.uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show()

                                // Arahkan ke halaman login setelah signup berhasil
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish() // Pastikan halaman signup tidak bisa diakses lagi setelah pindah ke login
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    if (task.exception?.message?.contains("The email address is already in use") == true) {
                        runOnUiThread {
                            Toast.makeText(this, "Email sudah digunakan. Silakan login.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "Signup gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    // Fungsi untuk menampilkan atau menyembunyikan progress bar
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSignup.isEnabled = !isLoading
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}
