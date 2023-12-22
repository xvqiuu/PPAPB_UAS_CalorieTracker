package com.example.calorietracker

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.calorietracker.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)


        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        // Ambil informasi role dari Firestore
        db.collection("user").document(userId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val username = documentSnapshot.getString("username")
                val email = documentSnapshot.getString("email")
                val tinggi = documentSnapshot.getString("tinggiBadan")
                val berat = documentSnapshot.getString("beratBadan")
                val target = documentSnapshot.getString("targetCalorie")
                binding.txtNama.text = username
                binding.txtEmail.text =  currentUser.email
                binding.txtHeight.text = "Tinggi: $tinggi"
                binding.txtWeight.text = "Berat: $berat"
                binding.txtTarget.text = "Target: $target"
            }

        with(binding) {
            btnLogout.setOnClickListener {
                val intent = Intent(this@ProfileFragment.requireActivity(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        return binding.root
    }
}