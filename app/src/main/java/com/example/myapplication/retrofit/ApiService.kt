package com.example.myapplication.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiService {

//    val BASE_URL: String = "https://pbd-backend-2024.vercel.app/"
//    val endpoint: ApiEndpoint
//        get() {
//            val retrofit = Retrofit.Builder()
//                .baseUrl( BASE_URL )
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//            return retrofit.create( ApiEndpoint::class.java )
//        }
private const val BASE_URL = "https://pbd-backend-2024.vercel.app/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: UserClient by lazy {
        retrofit.create(UserClient::class.java)
    }
}