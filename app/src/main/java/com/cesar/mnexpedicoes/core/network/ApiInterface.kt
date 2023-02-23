package com.cesar.mnexpedicoes.core.network

import com.cesar.mnexpedicoes.fragments.home.model.EventResponse
import com.cesar.mnexpedicoes.fragments.login.model.CheckCredencialsResponse
import com.cesar.mnexpedicoes.fragments.login.register.model.UserExistsResponse
import com.cesar.mnexpedicoes.fragments.login.register.model.UserResponse
import com.cesar.mnexpedicoes.utils.jsonObjectOf
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    // EVENT METHODS
    @GET("/getActiveEvents")
    fun getSchedule(): Call<ArrayList<EventResponse>>

    // USER METHODS
    @GET("/checkCredencials")
    fun checkCredencials(
        @Query("phone") phone: String,
        @Query("password") password: String
    ): Call<CheckCredencialsResponse>

    @GET("/checkIfUserExists")
    fun checkIfUserExists(
        @Query("phone") phone: String,
    ): Call<UserExistsResponse>

    @POST("/postUser")
    fun postUser(
        @Body params: JsonObject
    ): Call<UserResponse>
}