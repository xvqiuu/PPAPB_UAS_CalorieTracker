package com.example.calorietracker

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class History(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @ColumnInfo(name = "waktu")
    val waktu: String,

    @ColumnInfo(name = "nama")
    val nama: String,

    @ColumnInfo(name = "takaran")
    val takaran: String,

    @ColumnInfo(name = "jumlah")
    val jumlah: String,
)
