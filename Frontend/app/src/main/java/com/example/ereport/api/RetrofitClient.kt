package com.example.ereport.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:9090/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val reportApi: ReportApi by lazy {
        retrofit.create(ReportApi::class.java)
    }
    val ostrzezenieApi: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}