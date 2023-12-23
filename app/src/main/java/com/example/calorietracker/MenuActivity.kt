package com.example.calorietracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietracker.AdminActivity.Companion.menuCollectionRef
import com.example.calorietracker.AdminActivity.Companion.menuListLiveData
import com.example.calorietracker.databinding.ActivityMenuBinding
import com.google.firebase.firestore.FirebaseFirestore

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    private var totalCalories = 0// Hitung total kalori
    private val dashboardFragment = DashboardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Ambil nilai totalCalories dari intent jika ada
        totalCalories = intent.getIntExtra("TOTAL_CALORIES", 0)

        with(binding){
            btnAddcustom.setOnClickListener {
                val intent = Intent(this@MenuActivity, AddActivity::class.java)

                intent.putExtra("TOTAL_CALORIES", totalCalories)
                startActivity(intent)
            }
            btnBack.setOnClickListener {
                val intent = Intent(this@MenuActivity, MainActivity2::class.java)
                startActivity(intent)
            }
            buttonSearch.setOnClickListener {
                // panggil fungsi performSearch() ketika tombol search ditekan
                performSearch(textEditSearchFilter.text.toString())
            }
            rvMenu.apply {
                layoutManager = LinearLayoutManager(context)
            }
        }
        observeMenus()
        getAllMenus()
    }

    private fun getAllMenus() {
        menuCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("MenuActivity", "Error listening for menu changes",
                    error)
                return@addSnapshotListener
            }
            //loop data
            val menu = arrayListOf<Menu>()
            snapshots?.forEach {
                //isinya mengambil data data yang berupa documentReference
                    documentReference ->
                menu.add(
                    //id digunakan untuk update dan hapus
                    Menu(documentReference.id,
                        documentReference.get("nama").toString(),
                        documentReference.get("jumlah").toString()
                    )
                )
            }
            if (menu != null) {
                menuListLiveData.postValue(menu)
            }
        }
    }

    private fun observeMenus() {
        menuListLiveData.observe(this) { menu ->
            val menuAdapter = MenuAdapter(menu) { menu ->
                deleteMenu(menu)
            }
            binding.rvMenu.adapter = menuAdapter
        }
    }

    private fun deleteMenu(menu : Menu) {
        menuCollectionRef.document(menu.id.toString()).delete()
            .addOnFailureListener {
                Log.d("MainActivity", "Error deleting menu : ", it)
            }
    }

    private fun performSearch(query: String) {
        val db = FirebaseFirestore.getInstance()

        // membuat kueri untuk mencari menu berdasarkan query
        val searchQuery = db.collection("menu")
            .whereEqualTo("nama", query)

        // Menjalankan kueri asinkron di background thread
        searchQuery.get()
            .addOnSuccessListener { querySnapshot ->
                // Menangani hasil pencarian
                val searchResults = mutableListOf<Menu>()

                for (document in querySnapshot.documents) {
                    // Mapping data dari dokumen Firestore ke objek Menu
                    val menu = document.toObject(Menu::class.java)
                    menu?.let {
                        searchResults.add(it)
                    }
                }
                // Memperbarui tampilan RecyclerView di thread utama
                runOnUiThread {
                    updateRecyclerView(searchResults)
                }
            }
            .addOnFailureListener { e ->
                // menangani kegagalan pencarian
                Log.e("FirestoreSearch", "Error searching menu", e)
                // Mungkin ingin menangani kegagalan dengan menampilkan pesan kesalahan kepada pengguna
            }
    }

    private fun updateRecyclerView(searchResults: List<Menu>) {
        // memperbarui tampilan recycler view dengan hasil pencarian
        binding.rvMenu.adapter = MenuAdapter(searchResults) { menu ->
            val intent = Intent(this@MenuActivity, EditActivity::class.java)
            intent.putExtra("id", menu.id)
            intent.putExtra("nama", menu.nama)
            intent.putExtra("jumlah", menu.jumlah)
            startActivity(intent)
        }
    }

}