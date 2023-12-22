package com.example.calorietracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietracker.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    companion object {
        //akses firestore
        val firestore = FirebaseFirestore.getInstance()

        val menuListLiveData : MutableLiveData<List<Menu>>
                by lazy {
                    MutableLiveData<List<Menu>>()
                }

        val menuCollectionRef = firestore.collection("menu")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            fabAddadmin.setOnClickListener {
                val intent = Intent(this@AdminActivity, AddadminActivity::class.java)
                startActivity(intent)
            }
            btnLogout.setOnClickListener {
                val intent = Intent(this@AdminActivity, MainActivity::class.java)
                startActivity(intent)
            }
            btnSearch.setOnClickListener {
                // panggil fungsi performSearch() ketika tombol search ditekan
                performSearch(textEditSearchFilter.text.toString())
            }
            rvMenuAdmin.apply {
                layoutManager = LinearLayoutManager(context)
            }
        }
        observeMenus()
        getAllMenus()
    }

    private fun getAllMenus() {
        menuCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("MainActivity", "Error listening for menu changes",
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
            binding.rvMenuAdmin.adapter = menuAdapter
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
        binding.rvMenuAdmin.adapter = MenuAdapter(searchResults) { menu ->
            val intent = Intent(this@AdminActivity, EditActivity::class.java)
            intent.putExtra("id", menu.id)
            intent.putExtra("nama", menu.nama)
            intent.putExtra("jumlah", menu.jumlah)
            startActivity(intent)
        }
    }

}