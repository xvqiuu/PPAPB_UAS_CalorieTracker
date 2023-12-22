package com.example.calorietracker

import com.google.firebase.firestore.Exclude

data class Menu(
    //id merefer langsung ke id yang terdapat dalam firebase
    @set:Exclude @get:Exclude @Exclude var id: String? = "",
    //membuat atribut dengan tipe data String dengan nilai default String kosong
    var nama : String = "",
    var jumlah : String = "",

    )
