package com.dicoding.capstone.ui.auth.login

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.dicoding.capstone.ui.main.MainActivity
import com.dicoding.capstone.databinding.ActivityLoginBinding
import com.dicoding.capstone.ui.auth.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur aksi untuk tombol login
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        // Mengatur validasi untuk input email dan password
        setupValidation()

        // Observasi perubahan status loading
        loginViewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Observasi error message
        loginViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        // Observasi data pengguna
        loginViewModel.userLiveData.observe(this, Observer { user ->
            user?.let {
                // Arahkan ke MainActivity setelah login berhasil
                Toast.makeText(this, "Welcome ${user.email}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        playAnimation()

        binding.txtSignupNow.setOnClickListener {
            // Intent untuk berpindah ke halaman Signup
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk mengatur validasi form
    private fun setupValidation() {
        binding.edtEmailLogin.doOnTextChanged { text, _, _, _ ->
            binding.edtEmailLogin.error = when {
                text.isNullOrEmpty() -> "Email cannot be empty"
                !text.contains("@") -> "Email format is invalid"
                else -> null
            }
        }

        binding.edtPassLogin.doOnTextChanged { text, _, _, _ ->
            binding.edtPassLogin.error = when {
                text.isNullOrEmpty() -> "Password cannot be empty"
                text.length < 6 -> "Password must be at least 6 characters"
                else -> null
            }
        }
    }

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

        // Memanggil ViewModel untuk melakukan login
        loginViewModel.performLogin(email, password)
    }

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
