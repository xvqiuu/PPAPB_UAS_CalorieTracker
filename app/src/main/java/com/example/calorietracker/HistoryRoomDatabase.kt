package com.example.calorietracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class],
    version = 1,
    exportSchema = false)
abstract class HistoryRoomDatabase : RoomDatabase(){
    abstract fun HistoryDao() : HistoryDao?

    companion object {
        @Volatile
        private var INSTANCE : HistoryRoomDatabase ? = null
        fun getDatabase(context: Context) : HistoryRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(HistoryRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryRoomDatabase::class.java, "history_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}