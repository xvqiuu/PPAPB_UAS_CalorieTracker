package com.example.calorietracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.calorietracker.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // menggunakan Handler untuk menampilkan splash screen selama beberapa detik
        Handler().postDelayed({
            // setelah beberapa detik, pindah ke aktivitas selanjutnya
            val SplashActivity = Intent(this@SplashActivity, WelcomeActivity::class.java)
            startActivity(SplashActivity)
        },3000)
    }
}