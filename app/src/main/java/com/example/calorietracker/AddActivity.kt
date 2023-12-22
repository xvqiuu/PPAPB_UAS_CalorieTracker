package com.example.calorietracker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.calorietracker.databinding.ActivityAddBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var mHistoryDao: HistoryDao
    private lateinit var executorService: ExecutorService

    private val channelId = "MenuNotification"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = HistoryRoomDatabase.getDatabase(this)
        mHistoryDao = db!!.HistoryDao()!!

        // buat saluran notifikasi (Notification Channel)
        createNotificationChannel()

        with(binding) {

            btnAdd.setOnClickListener {
                insert(
                    History(
                        waktu = editWaktuMakan.text.toString(),
                        nama = editNamaMakanan.text.toString(),
                        takaran = editTakaranSaji.text.toString(),
                        jumlah = editJumlahKalori.text.toString()
                    )
                )


                showNotification("History berhasil ditambahkan!")

               finish()
            }
            btnBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
    private fun insert(history: History) {
        executorService.execute {
            mHistoryDao.insert(history)
        }
    }
    private fun createNotificationChannel() {
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Menu Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun showNotification(message: String) {
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Notifikasi Menu")
            .setContentText("Menu Berhasil Ditambahkan")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(notificationId, builder.build())
    }
}