package com.example.calorietracker

import com.google.firebase.firestore.Exclude

data class User(
    //id merefer langsung ke id yang terdapat dalam firebase
    @set:Exclude @get:Exclude @Exclude var id: String? = "",
    //membuat atribut dengan tipe data String dengan nilai default String kosong
    var username: String = "",
    var targetCalorie: String = "",
    var tinggiBadan: String = "",
    var beratBadan: String = "",
    var role : String = "user"
)
