package com.example.ereport.api

import com.example.ereport.model.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


import com.example.ereport.model.LoginRequest
import com.example.ereport.model.LoginResponse
import com.example.ereport.model.Miasto
import com.example.ereport.model.RegisterResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface UserApi {

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body request: UserRequest): Call<RegisterResponse>

    @POST("uzytkownicy")
    fun addUser(@Body user: UserRequest): Call<Unit>

    // --- Dodaj te trzy ---
    @GET("powiaty")
    fun getPowiaty(@Query("wojewodztwo") wojewodztwo: String): Call<List<String>>

    @GET("gminy")
    fun getGminy(@Query("powiat") powiat: String): Call<List<String>>

    @GET("miejscowosci")
    fun getMiejscowosci(@Query("gmina") gmina: String): Call<List<Miasto>>
}