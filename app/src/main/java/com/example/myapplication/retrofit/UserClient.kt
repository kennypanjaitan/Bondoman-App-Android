package com.example.myapplication.retrofit

import retrofit2.Call
import com.example.myapplication.models.LoginModel
import retrofit2.http.Body
import retrofit2.http.POST

interface UserClient {

    @POST("api/auth/login")
    fun login(@Body loginModel: LoginModel): Call<LoginResponse>

}