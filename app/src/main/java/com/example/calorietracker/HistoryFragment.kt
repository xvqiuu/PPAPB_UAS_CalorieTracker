package com.example.calorietracker

import HistoryAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietracker.databinding.FragmentHistoryBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class HistoryFragment : Fragment() {
    private lateinit var binding : FragmentHistoryBinding
    //menyimpan instance dari DAO (Data Access Object)
    private lateinit var mHistoryDao: HistoryDao
    //digunakan untuk menjalankan tugas secara tidak langsung pada thread terpisah
    private lateinit var executorService: ExecutorService

    private var totalCalories: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        executorService = Executors.newSingleThreadExecutor()
        val db = HistoryRoomDatabase.getDatabase(this.requireActivity())
        mHistoryDao = db!!.HistoryDao()!!
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnTambahMakanan.setOnClickListener {
                val intent =  Intent(this@HistoryFragment.requireActivity(), MenuActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getAllHistorys() {
        //memperbarui tampilan recycler view dengan History
        mHistoryDao.allHistorys.observe(this) {
            binding.rvHistory.apply {
                adapter = HistoryAdapter(it, { history ->
                    val intent = Intent(this@HistoryFragment.requireActivity(), EditActivity::class.java)
                    intent.putExtra("id",history.id)
                    intent.putExtra("waktu",history.waktu)
                    intent.putExtra("nama",history.nama)
                    intent.putExtra("takaran",history.takaran)
                    intent.putExtra("jumlah",history.jumlah)
                    startActivity(intent)
                })
            }

            // Set adapter dan layout manager pada RecyclerView
            with(binding){
                rvHistory.apply {
                    layoutManager = LinearLayoutManager(context)
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllHistorys()
    }
}
