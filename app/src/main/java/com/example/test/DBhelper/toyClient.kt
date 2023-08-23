package com.example.test.DBhelper

import com.example.test.services.AuthService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object toyClient {

    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

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
//    val okHttpClient = OkHttpClient.Builder()
//        .connectTimeout(120, TimeUnit.SECONDS)
//        .readTimeout(120,TimeUnit.SECONDS)
//        .writeTimeout(120, TimeUnit.SECONDS)
//        .build()
//    var gson = GsonBuilder().setLenient().create()
//
//    private const val baseurl = "http://127.0.0.1:5000/"
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(baseurl)
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .client(okHttpClient)
//        .build()
//
////    val service = retrofit.create(toyClient::class.java)
//    val authService : LoginService by lazy { retrofit.create(LoginService::class.java) }


}