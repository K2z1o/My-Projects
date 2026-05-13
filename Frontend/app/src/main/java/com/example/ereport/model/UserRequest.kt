package com.example.ereport.model

data class UserRequest (
    val typ: String = "O",
    val imie: String,
    val nazwisko: String,
    val email: String,
    val telefon: String,
    val haslo: String,
    val miejscowoscId: String
)