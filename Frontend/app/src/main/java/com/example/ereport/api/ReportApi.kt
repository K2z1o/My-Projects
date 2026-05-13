package com.example.ereport.api

import com.example.ereport.model.ReportRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApi {

    @POST("/api/zgloszenia")
    suspend fun sendReport(
        @Body request: ReportRequest
    )
}