package com.example.test.services

import com.example.test.model.DiningData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DBService {
    @GET("db/request")
    fun request(@Query("page") page : Int): Call<ArrayList<DiningData>>

}