package com.awaken.appm.android.network

import com.awaken.appm.android.model.Balance
import com.awaken.appm.android.model.Categories
import com.awaken.appm.android.model.PostReply
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SheetApiService {

    @GET("exec")
    suspend fun getCategories(
        @Query("spreadSheetId") spreadSheetId: String,
        @Query("sheetName") sheetName: String,
        @Query("startRow") startRow: Int,
        @Query("startColumn") startColumn: Int,
        @Query("numRows") numRows: Int,
        @Query("numColumns") numColumns: Int
    ) : Response<Categories>

    @GET("exec")
    suspend fun getMoney(
        @Query("spreadSheetId") spreadSheetId: String,
        @Query("sheetName") sheetName: String,
        @Query("startRow") startRow: Int,
        @Query("startColumn") startColumn: Int,
        @Query("numRows") numRows: Int,
        @Query("numColumns") numColumns: Int
    ) : Response<Balance>

    @POST("exec")
    suspend fun insertData(
        @Query("spreadSheetId") spreadSheetId: String,
        @Query("sheetName") sheetName: String,
        @Query("drk") drk: String,
        @Query("category") category: String,
        @Query("date") date: String,
        @Query("money") money: String,
        @Query("comment") comment: String
    ) : Response<PostReply>
}