package com.example.calorietracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.calorietracker.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        with(binding) {
            btnRegister.setOnClickListener {
                val inputUsername = editTxtUsernameRegister.text.toString()
                val inputEmail = editTxtEmailRegister.text.toString()
                val inputTarget = editTxtTargetCalorie.text.toString()
                val inputTinggi = editTxtHeight.text.toString()
                val inputBerat = editTxtWeight.text.toString()
                val inputPassword = editTxtPasswordRegister.text.toString()

                if (inputUsername.isEmpty()) {
                    editTxtUsernameRegister.error = "Username harus diisi"
                    return@setOnClickListener
                }
                else if (inputEmail.isEmpty()) {
                    editTxtEmailRegister.error = "Email harus diisi"
                    return@setOnClickListener
                }
                else if (inputPassword.isEmpty()) {
                    editTxtPasswordRegister.error = "Password harus diisi"
                    return@setOnClickListener
                }
                else {
                    auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                val userId = user?.uid

                                val newUser = User(
                                    id = userId.toString(),
                                    username = inputUsername,
                                    targetCalorie = inputTarget,
                                    tinggiBadan = inputTinggi,
                                    beratBadan = inputBerat
                                )
                                addUser(newUser)
                            }

                            editTxtUsernameRegister.text.clear()
                            editTxtEmailRegister.text.clear()
                            editTxtTargetCalorie.text.clear()
                            editTxtHeight.text.clear()
                            editTxtWeight.text.clear()
                            editTxtPasswordRegister.text.clear()

                            val activity = requireActivity() as MainActivity
                            activity.switchToLoginTab()

                        }
                }
            }
            // Set click listener for the "Register" text
            txtLogin.setOnClickListener {
                val activity = requireActivity() as MainActivity
                activity.switchToLoginTab()
            }
            return binding.root
        }
    }

    private fun addUser(user : User){
        user.id?.let {
            db.collection("user")
                // menggunakan document dengan id yang sama dengan user.id
                .document(it)
                .set(user)
                .addOnFailureListener { e ->
                    Log.d("MainActivity", "Error adding user : ", e)
                    Toast.makeText(
                        requireContext(),
                        "Insert Failed",
                        Toast.LENGTH_SHORT
                    ).show();
                }
        }
    }
}