package com.example.calorietracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calorietracker.databinding.ActivityEditBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditBinding
    private lateinit var mHistoryDao: HistoryDao
    private lateinit var executorService: ExecutorService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = HistoryRoomDatabase.getDatabase(this)
        mHistoryDao = db!!.HistoryDao()!!

        with(binding) {
            editWaktuMakan.setText(intent.getStringExtra("waktu"))
            editNamaMakanan.setText(intent.getStringExtra("nama"))
            editTakaranSaji.setText(intent.getStringExtra("takaran"))
            editJumlahKalori.setText(intent.getStringExtra("jumlah"))

            btnSave.setOnClickListener {
                update(
                    History(
                        id = intent.getIntExtra("id",0),
                        waktu = editWaktuMakan.text.toString(),
                        nama = editNamaMakanan.text.toString(),
                        takaran = editTakaranSaji.text.toString(),
                        jumlah = editJumlahKalori.text.toString()
                    ))
            }
        }
    }

    private fun update(history: History) {
        executorService.execute {
            mHistoryDao.update(history)

            runOnUiThread {
                finish()
                Toast.makeText(
                    this@EditActivity,
                    "Menu Berhasil Disimpan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}