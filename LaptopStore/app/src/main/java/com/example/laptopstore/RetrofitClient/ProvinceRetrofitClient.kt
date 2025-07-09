package com.example.laptopstore.RetrofitClient

import com.example.laptopstore.api.Province
import com.example.laptopstore.api.ProvinceApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object ProvinceRetrofitClient {

    private const val BASE_URL = "https://provinces.open-api.vn/api/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val apiService: ProvinceApiService by lazy {
        retrofit.create(ProvinceApiService::class.java)
    }
}

