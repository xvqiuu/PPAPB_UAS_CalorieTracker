package com.example.calorietracker

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calorietracker.AdminActivity
import com.example.calorietracker.Menu
import com.example.calorietracker.databinding.ActivityAddadminBinding


class AddadminActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddadminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddadminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnAdd.setOnClickListener {
                //mengambil nilai yg diinputkan
                val nama = editNamaMakanan.text.toString()
                val jumlah = editJumlahKalori.text.toString()


                //membuat objek baru dr kelas student yang berisi nilai- nilai tersebut
                val newMenu = Menu(
                    nama = nama, jumlah = jumlah
                )
                addMenu(newMenu)

                Toast.makeText(this@AddadminActivity, "Menu Added Successfully",
                    Toast.LENGTH_SHORT).show()
                finish()
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun addMenu(menu : Menu) {
        AdminActivity.menuCollectionRef.add(menu)
            .addOnFailureListener {
                Log.d("AdminActivity", "Error adding menu : ", it)
            }
    }
}