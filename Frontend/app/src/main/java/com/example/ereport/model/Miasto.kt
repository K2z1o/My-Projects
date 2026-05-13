package com.example.ereport.model

data class Miasto (
    val id: String,
    val nazwa: String
) {
    override fun toString(): String {
        return nazwa
    }
}

