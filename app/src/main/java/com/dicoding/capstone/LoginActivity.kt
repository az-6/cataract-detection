package com.dicoding.capstone

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.dicoding.capstone.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    // Inisialisasi ViewBinding
    private lateinit var binding: ActivityLoginBinding

    // Inisialisasi FirebaseAuth
    private lateinit var mAuth: FirebaseAuth

    // Inisialisasi Firestore
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Set content view dengan binding.root
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth dan Firestore
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Mengatur aksi untuk tombol login
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        // Mengatur validasi untuk input email dan password
        setupValidation()
        playAnimation()

        // Menambahkan aksi ketika klik pada "Signup Now"
        binding.txtSignupNow.setOnClickListener {
            // Intent untuk berpindah ke halaman Signup
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk mengatur validasi form
    private fun setupValidation() {
        // Validasi email
        binding.edtEmailLogin.doOnTextChanged { text, _, _, _ ->
            binding.edtEmailLogin.error = when {
                text.isNullOrEmpty() -> "Email cannot be empty"
                !text.contains("@") -> "Email format is invalid"
                else -> null
            }
        }

        // Validasi password
        binding.edtPassLogin.doOnTextChanged { text, _, _, _ ->
            binding.edtPassLogin.error = when {
                text.isNullOrEmpty() -> "Password cannot be empty"
                text.length < 6 -> "Password must be at least 6 characters"
                else -> null
            }
        }
    }

    // Fungsi untuk menangani proses login
    private fun handleLogin() {
        val email = binding.edtEmailLogin.text.toString().trim()
        val password = binding.edtPassLogin.text.toString().trim()

        // Validasi form saat tombol login diklik
        if (email.isEmpty()) {
            binding.edtEmailLogin.error = "Email is required"
            return
        }
        if (password.isEmpty()) {
            binding.edtPassLogin.error = "Password is required"
            return
        }

        // Validasi tambahan untuk email dan password
        if (!email.contains("@")) {
            binding.edtEmailLogin.error = "Invalid email format"
            return
        }
        if (password.length < 6) {
            binding.edtPassLogin.error = "Password must be at least 6 characters"
            return
        }

        // Menampilkan progress bar selama login
        showLoading(true)

        // Melakukan autentikasi dengan Firebase
        performFirebaseLogin(email, password)
    }

    // Fungsi untuk melakukan login menggunakan Firebase Authentication
    private fun performFirebaseLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val user: FirebaseUser? = mAuth.currentUser
                    if (user != null) {
                        // Ambil data pengguna dari Firestore
                        getUserDataFromFirestore(user.uid)
                    }
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Fungsi untuk mengambil data pengguna dari Firestore berdasarkan UID
    private fun getUserDataFromFirestore(uid: String) {
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    val email = document.getString("email")

                    // Menampilkan data pengguna
                    Toast.makeText(this, "Welcome $name", Toast.LENGTH_SHORT).show()

                    // Arahkan ke MainActivity setelah login berhasil
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "User data not found in Firestore", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to get user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Fungsi untuk menampilkan atau menyembunyikan progress bar
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val movingImg: Long = 6000

        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = movingImg
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}
