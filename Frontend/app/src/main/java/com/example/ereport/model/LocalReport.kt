/*
LocalReport.kt – model danych przechowywany lokalnie (offline queue)
ReportDao.kt – operacje na lokalnej bazie
AppDatabase.kt – konfiguracja Room

Lokalny storage przy użyciu Android Room

To jest warstwa offline do przechowywania zgłoszeń bez internetu.

/Bartek
*/

package com.example.ereport.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//To będzie tabela w bazie danych:

@Entity(tableName = "reports")
data class LocalReport(
    @PrimaryKey val id: String,
    val lat: Double,
    val lon: Double,
    val animal: String,
    val eventTime: Long,
    val createdAtLocal: Long,
    val localImageUri: String?,
    var imageUrl: String?,
    var status: ReportStatus,
    var retryCount: Int
)

enum class ReportStatus {
    PENDING,
    SENT,
    FAILED
}