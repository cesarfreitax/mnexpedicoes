package com.cesar.mnexpedicoes.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val url by lazy {
        Retrofit.Builder()
            .client(Interceptors.client())
            .baseUrl("https://mnexpedicoes.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiInterface: ApiInterface by lazy {
        url.create(ApiInterface::class.java)
    }
}