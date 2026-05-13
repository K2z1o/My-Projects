package com.example.ereport.model

data class ReportRequest(
    val lat: Double,
    val lon: Double,
    val animal: String,
    val eventTime: String,
    val imageUrl: String?
)