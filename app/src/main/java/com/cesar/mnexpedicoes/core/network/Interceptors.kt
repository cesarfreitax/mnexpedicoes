package com.cesar.mnexpedicoes.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class Interceptors {

    class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", apiKey)
                .build()
            return chain.proceed(request)
        }
    }

    companion object {
        fun client() : OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            return OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor("7L7K6NnWXQa6Fa6uwU4J51Vpp5rYyAYk1b1LFap9"))
                .addInterceptor(loggingInterceptor)
                .build()
        }
    }

}