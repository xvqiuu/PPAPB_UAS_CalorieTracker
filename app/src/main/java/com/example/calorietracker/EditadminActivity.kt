package com.example.calorietracker

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calorietracker.databinding.ActivityEditadminBinding

class EditadminActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditadminBinding

    private var updateId = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditadminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            editNamaMakanan.setText(intent.getStringExtra("nama"))
            editJumlahKalori.setText(intent.getStringExtra("jumlah"))

            btnEdit.setOnClickListener {
                val updateMenu = Menu (
                    id = intent.getStringExtra("id"),
                    nama = editNamaMakanan.text.toString(),
                    jumlah = editJumlahKalori.text.toString(),

                )
                updateMenu(updateMenu)
                //diperbarui dengan nilai baru dari objek student
                updateId = ""
                finish()
            }
        }
    }

    private fun updateMenu(menu : Menu) {
        AdminActivity.menuCollectionRef.document(menu.id.toString()).set(menu)
            .addOnFailureListener {
                Log.d("AdminActivity", "Error updating menu : ", it)

                runOnUiThread {
                    finish()
                    Toast.makeText(
                        this@EditadminActivity,
                        "Menu Saved Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
