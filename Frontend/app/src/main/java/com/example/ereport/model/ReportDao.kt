/*
LocalReport.kt – model danych przechowywany lokalnie (offline queue)
ReportDao.kt – operacje na lokalnej bazie
AppDatabase.kt – konfiguracja Room

Lokalny storage przy użyciu Android Room

To jest warstwa offline do przechowywania zgłoszeń bez internetu.

/Bartek
*/

package com.example.ereport.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//To będzie warstwa która rozmawia z bazą danych

@Dao
interface ReportDao {

    @Insert
    suspend fun insert(report: LocalReport)

    @Query("SELECT * FROM reports WHERE status = 'PENDING'")
    fun getPendingReports(): List<LocalReport>

    @Update
    suspend fun update(report: LocalReport)

    @Query("SELECT * FROM reports ORDER BY createdAtLocal DESC")
    fun observeReports(): Flow<List<LocalReport>>

    @Query("UPDATE reports SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("DELETE FROM reports WHERE id = :id")
    suspend fun delete(id: String)
}