package com.dicoding.capstone.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.capstone.ui.main.MainActivity
import com.dicoding.capstone.R
import com.dicoding.capstone.ui.dashboard.WelcomeActivity
import com.dicoding.capstone.databinding.ActivityOnBoardingBinding
import com.google.firebase.auth.FirebaseAuth

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    // Inisialisasi FirebaseAuth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Cek apakah user sudah login
        checkUserLoggedIn()

        // Hide action bar
        supportActionBar?.hide()

        // Load PNG images with Glide
        Glide.with(this)
            .asBitmap()  // Remove asGif() and use asBitmap() for PNGs
            .load(R.drawable.logo_1)  // Replace with PNG image
            .into(binding.logoMenu1)

        Glide.with(this)
            .asBitmap()  // Same for this image
            .load(R.drawable.logo_2)  // Replace with PNG image
            .into(binding.logoMenu2)

        Glide.with(this)
            .asBitmap()  // Same for this image
            .load(R.drawable.logo_3)  // Replace with PNG image
            .into(binding.logoMenu3)

        Glide.with(this)
            .asBitmap()  // Same for this image
            .load(R.drawable.logo_4)  // Replace with PNG image
            .into(binding.logoMenu4)



    // Menangani klik tombol start
        binding.btnStart.setOnClickListener {
            // Jika belum login, arahkan ke WelcomeActivity
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    // Fungsi untuk mengecek apakah pengguna sudah login
    private fun checkUserLoggedIn() {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // Jika pengguna sudah login, langsung ke MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // Pastikan tidak bisa kembali ke halaman OnBoarding
        }
    }
}
