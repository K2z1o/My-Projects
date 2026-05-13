package com.example.ereport.data.sync

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.example.ereport.model.ReportRequest
import com.example.ereport.api.RetrofitClient
import com.example.ereport.model.AppDatabase
import com.example.ereport.model.LocalReport
import com.example.ereport.model.ReportStatus
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resumeWithException

class SyncService(
    private val context: Context
) {

    private val db = AppDatabase.getDatabase(context)
    private val dao = db.reportDao()

    private val MAX_RETRIES = 20

    suspend fun syncReports() {

        val pendingReports = dao.getPendingReports()

        for (report in pendingReports) {
            try {

                var updatedReport = report

                if (updatedReport.imageUrl == null && updatedReport.localImageUri != null) {
                    val uri = Uri.parse(report.localImageUri)
                    val url = uploadImageToFirebase(uri)
                    updatedReport = updatedReport.copy(imageUrl = url)
                }

                sendToBackend(updatedReport)
                dao.update(updatedReport.copy(status = ReportStatus.SENT))
            } catch (e: Exception) {
                handleFailure(report)
            }
        }
    }

    private suspend fun sendToBackend(report: LocalReport) {

        val request = ReportRequest(
            lat = report.lat,
            lon = report.lon,
            animal = report.animal,
            eventTime = report.eventTime.toString(),
            imageUrl = report.imageUrl
        )

        RetrofitClient.reportApi.sendReport(request)
    }

    private suspend fun handleFailure(report: LocalReport) {

        report.retryCount += 1 // błąd. zwiększamy
        //max prób ustawiłem sztywno na 20. 20 razy co 30 sekund będzie próbowało wysłać.
        if (report.retryCount >= MAX_RETRIES) {
            report.status = ReportStatus.FAILED
        } else {
            report.status = ReportStatus.PENDING
        }
        dao.update(report)
    }

    private suspend fun uploadImageToFirebase(uri: Uri): String {

        return suspendCancellableCoroutine { cont ->

            val storage = FirebaseStorage.getInstance()
            val ref = storage.reference
                .child("zdjecia/${UUID.randomUUID()}.jpg")

            ref.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    ref.downloadUrl
                }
                .addOnSuccessListener { downloadUri ->
                    cont.resume(downloadUri.toString(), null)
                }
                .addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }
    }
}