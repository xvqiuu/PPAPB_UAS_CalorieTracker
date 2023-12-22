package com.example.calorietracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calorietracker.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras

        with(binding) {
            detWaktu.text = "${data?.getString("waktu")}"
            detNama.text = "${data?.getString("nama")}"
            detTakaran.text = "${data?.getString("takaran")}"
            detJumlah.text = "${data?.getString("jumlah")}"

            btnBack.setOnClickListener {
                val intent = Intent(this@DetailActivity,HistoryFragment::class.java)
                startActivity(intent)
            }
        }
    }
}