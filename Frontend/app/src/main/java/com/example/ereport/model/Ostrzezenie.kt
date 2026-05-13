package com.example.ereport.model



import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


data class Ostrzezenie(
    val id: String? = null,
    val Tytul: String = "",
    val Grupa: String = "",
    val MiejscowoscId: String = "",
    val CzasRozpoczecia: String? = null,
    val CzasZakonczenia: String? = null,
    val Opis: String = ""
) {
    fun formatCzas(): String {
        val od = CzasRozpoczecia?.replace("T", " ")?.replace("Z", "") ?: "?"
        val do_ = CzasZakonczenia?.replace("T", " ")?.replace("Z", "") ?: "?"
        return "$od – $do_"
    }

    fun getStatus(): StatusOstrzezenia {
        val now = System.currentTimeMillis()
        val start = CzasRozpoczecia?.let { parseToMillis(it) }
        val end = CzasZakonczenia?.let { parseToMillis(it) }
        return when {
            start != null && now < start -> StatusOstrzezenia.NADCHODZACE
            end != null && now > end -> StatusOstrzezenia.ZAKONCZONE
            else -> StatusOstrzezenia.AKTYWNE
        }
    }

    private fun parseToMillis(dateStr: String): Long {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
            sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
            sdf.parse(dateStr)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}