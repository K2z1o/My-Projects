package com.example.ereport.api

import com.example.ereport.model.Powiat
import com.example.ereport.model.Gmina
import com.example.ereport.model.LoginRequest
import com.example.ereport.model.LoginResponse
import com.example.ereport.model.Miasto
import com.example.ereport.model.Ostrzezenie
import com.example.ereport.model.RegisterResponse
import com.example.ereport.model.UserRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

data class PowiatResponse(
    val powiaty: List<Powiat>
)
data class GminaResponse(
    val gminy: List<Gmina>
)
interface ApiService {
    @GET("powiaty")
    fun getPowiaty(@Query("wojewodztwo") woj: String): Call<List<String>>

    @GET("gminy")
    fun getGminy(@Query("powiat") powiat: String): Call<List<String>>

    @GET("miejscowosci")
    fun getMiejscowosci(@Query("gmina") gmina: String): Call<List<Miasto>>

    // logowanie
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // rejestracja
    @POST("auth/register")
    fun register(@Body request: UserRequest): Call<RegisterResponse>

    @GET("ostrzezenia")
    fun getOstrzezenia(
        @Query("miejscowoscId") miejscowoscId: String,
        @Query("sortBy") sortBy: String = "Tytul"
    ): Call<List<Ostrzezenie>>

    @DELETE("ostrzezenia/{id}")
    fun deleteOstrzezenie(@Path("id") id: String): Call<Void>

}
