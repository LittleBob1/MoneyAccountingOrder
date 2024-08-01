package com.awaken.appm.android.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SheetApiClient {
    private const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbya2puQeJoMoV2zYkyfjtUHr3gH3owNujhhkKMR-FdJLiMAAvzed1dGVhRmDX1EXUhCvg/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: SheetApiService = retrofit.create(SheetApiService::class.java)
}