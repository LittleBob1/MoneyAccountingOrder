package com.awaken.appm.android.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SheetApiClient {
    private const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbxt-AqSAP_5VQjJwh4XMb-FnPHJs6A2gzqtvj0acG7uvgQHNuimF66HLqMBFMMWeo-M/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: SheetApiService = retrofit.create(SheetApiService::class.java)
}