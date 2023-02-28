package com.cesar.mnexpedicoes.core.network

import com.cesar.mnexpedicoes.features.home.model.EventResponse
import com.cesar.mnexpedicoes.features.login.model.CheckCredencialsResponse
import com.cesar.mnexpedicoes.features.login.register.model.UserExistsResponse
import com.cesar.mnexpedicoes.features.login.register.model.UserResponse
import com.cesar.mnexpedicoes.features.profile.editprofile.model.EditProfileUserResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // EVENT METHODS
    @GET("/getActiveEvents")
    fun getSchedule(): Call<ArrayList<EventResponse>>

    // USER METHODS
    @GET("/getUserByPhone/{phone}")
    fun getUserByPhone(
        @Path("phone") phone: String
    ): Call<UserResponse>

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
    @POST("/updateUserByPhone/{phone}")
    fun updateUserByPhone(
        @Path("phone") phone: String,
        @Body params: JsonObject
    ): Call<EditProfileUserResponse>
}