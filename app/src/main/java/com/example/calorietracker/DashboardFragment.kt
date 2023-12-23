package com.example.calorietracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calorietracker.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding : FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Mengambil data dari arguments
        val totalCalories = arguments?.getInt("TOTAL_CALORIES") ?: 0

        // Melakukan sesuatu dengan data yang diterima, contohnya:
        binding.txtViewSisaKalori.text = "$totalCalories"

    }

}