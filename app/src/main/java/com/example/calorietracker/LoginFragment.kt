package com.example.calorietracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.calorietracker.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    //buat binding dulu
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        val SharedPreferences = SharedPreference(requireActivity())
        // Set click listener for the login button
        with(binding) {
            checkboxLogin.setOnCheckedChangeListener { buttonView, isChecked ->
                btnLogin.isEnabled = isChecked
            }
            btnLogin.setOnClickListener {
                val email = editTxtEmailLogin.text.toString()
                val password = editTxtPasswordLogin.text.toString()

                if (email.isEmpty()) {
                    editTxtEmailLogin.error = "Email harus diisi"
                    return@setOnClickListener

                } else if (password.isEmpty()) {
                    editTxtPasswordLogin.error = "Password harus diisi"
                    return@setOnClickListener

                } else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                val currentUser = auth.currentUser
                                val userId = currentUser?.uid

                                // Ambil informasi role dari Firestore
                                db.collection("user").document(userId!!)
                                    .get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        val role = documentSnapshot.getString("role")

                                        if (role == "admin") {
                                            // Navigasi ke halaman admin
                                            val intent = Intent(
                                                this@LoginFragment.requireActivity(),
                                                AdminActivity::class.java
                                            )
                                            startActivity(intent)
                                        } else {
                                            // Navigasi ke halaman user biasa
                                            val intent = Intent(
                                                this@LoginFragment.requireActivity(),
                                                MainActivity2::class.java
                                            )
                                            startActivity(intent)
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            requireContext(),
                                            "Failed to fetch user role: $e",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            // Set click listener for the "Register" text
            txtRegister.setOnClickListener {
                val activity = requireActivity() as MainActivity
                activity.switchToLoginTab()
            }
            return binding.root
        }
    }
}