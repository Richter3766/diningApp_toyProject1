package com.example.test.helper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object toyClient {

    private var instance: Retrofit? = null

    private const val baseurl = "http://43.202.37.238:5000/"

    fun getInstance() : Retrofit{
        if(instance == null){
            instance = Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return instance!!
    }
}