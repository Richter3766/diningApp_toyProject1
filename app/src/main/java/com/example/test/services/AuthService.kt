package com.example.test.services

import com.example.test.model.LoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService{
    @POST("auth/signIn")
    fun verifyToken(@Query("token") token: String?): Call<LoginData>
}