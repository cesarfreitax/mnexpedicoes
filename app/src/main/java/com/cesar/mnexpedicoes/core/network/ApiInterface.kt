package com.cesar.mnexpedicoes.core.network

import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    // EVENT METHODS
    @GET("/getActiveEvents")
    fun getSchedule(): Call<ArrayList<EventResponse>>
}