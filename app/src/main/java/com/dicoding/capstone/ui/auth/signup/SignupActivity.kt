package com.dicoding.capstone.ui.auth.signup

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.capstone.databinding.ActivitySignupBinding
import com.dicoding.capstone.ui.auth.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    // Using ViewModel
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe loading state and toast messages
        signupViewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        signupViewModel.toastMessage.observe(this, Observer { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        })

        // Observe validation errors
        signupViewModel.nameError.observe(this, Observer { error ->
            binding.edtName.error = error
        })
        signupViewModel.emailError.observe(this, Observer { error ->
            binding.edtEmail.error = error
        })
        signupViewModel.passwordError.observe(this, Observer { error ->
            binding.edtPass.error = error
        })

        // Handle Signup button click
        binding.btnSignup.setOnClickListener {
            handleSignup()
        }

        // Navigate to Login activity
        binding.txtLoginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        playAnimation()
    }

    private fun handleSignup() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPass.text.toString().trim()

        // Validate inputs
        signupViewModel.validateInputs(name, email, password)

        // If no validation errors, proceed with signup
        if (signupViewModel.nameError.value == null &&
            signupViewModel.emailError.value == null &&
            signupViewModel.passwordError.value == null) {

            // Perform signup
            signupViewModel.performSignup(name, email, password)
        }
    }

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
