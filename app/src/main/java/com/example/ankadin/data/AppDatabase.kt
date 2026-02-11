package com.example.ankadin.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ankadin.data.AcilKisiDao
import com.example.ankadin.data.AcilKisiEntity
import com.example.ankadin.data.AcilMesajDao
import com.example.ankadin.data.AcilMesajEntity
import com.example.ankadin.data.UserProfileDao
import com.example.ankadin.data.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        AcilKisiEntity::class,
        AcilMesajEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun acilKisiDao(): AcilKisiDao
    abstract fun acilMesajDao(): AcilMesajDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ankadin_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
