/*
LocalReport.kt – model danych przechowywany lokalnie (offline queue)
ReportDao.kt – operacje na lokalnej bazie
AppDatabase.kt – konfiguracja Room

Lokalny storage przy użyciu Android Room

To jest warstwa offline do przechowywania zgłoszeń bez internetu.

/Bartek
*/

package com.example.ereport.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LocalReport::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reportDao(): ReportDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reports-db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}