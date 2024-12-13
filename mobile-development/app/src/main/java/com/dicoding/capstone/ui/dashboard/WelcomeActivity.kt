package com.dicoding.capstone.ui.dashboard

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.capstone.databinding.ActivityWelcomeBinding
import com.dicoding.capstone.ui.auth.login.LoginActivity
import com.dicoding.capstone.ui.auth.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    // Using ViewModel
    private val welcomeViewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide ActionBar
        supportActionBar?.hide()

        // Observe any data changes in ViewModel (e.g., welcome message)
        welcomeViewModel.welcomeMessage.observe(this) { message ->
            binding.titleTextView.text = message
        }

        // Call the animation functions
        playAnimation()
        playTextViewAnimation()

        // Button click listeners
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val movingImg: Long = 6000

        // Add translation animation to the imageView
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = movingImg
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    @SuppressLint("Recycle")
    private fun playTextViewAnimation() {
        // Fade in animations for various UI elements
        ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).apply {
            duration = 3000
        }.start()

        ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 0f, 1f).apply {
            duration = 3000
        }.start()

        ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).apply {
            duration = 3000
        }.start()

        ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).apply {
            duration = 3000
        }.start()
    }
}
