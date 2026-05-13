package com.example.ereport.model

data class LoginResponse(
    val success: Boolean,
    val miejscowoscId: String? = null,
    val userId: String? = null  // opcjonalnie, jeśli backend zwraca
)