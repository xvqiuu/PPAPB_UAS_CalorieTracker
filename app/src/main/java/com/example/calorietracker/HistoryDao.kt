package com.example.calorietracker

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: History)

    @Update
    fun update(history: History)

    @Delete
    fun delete(history: History)

    @get:Query("SELECT * from history_table ORDER BY id ASC")
    val allHistorys: LiveData<List<History>>

    @Query("SELECT * FROM history_table WHERE nama LIKE '%' || :query || '%'")
    fun searchHistorys(query: String): List<History>
}