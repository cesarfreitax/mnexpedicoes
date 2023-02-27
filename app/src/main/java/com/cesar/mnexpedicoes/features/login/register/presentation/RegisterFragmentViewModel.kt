package com.cesar.mnexpedicoes.features.login.register.presentation

import androidx.lifecycle.ViewModel
import com.cesar.mnexpedicoes.core.network.RetrofitInstance
import com.cesar.mnexpedicoes.features.login.register.model.UserExistsResponse
import com.cesar.mnexpedicoes.features.login.register.model.UserResponse
import com.cesar.mnexpedicoes.features.login.register.model.toJsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragmentViewModel : ViewModel() {

    var userCreatedSuccessfull = false
    var userExists = false

    fun postUser(
        userResponse: UserResponse,
        callback: () -> Unit
    ) {
        RetrofitInstance.apiInterface.postUser(userResponse.toJsonObject())
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    userCreatedSuccessfull = response.isSuccessful
                    callback()
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    throw (Exception(t.message))
                }
            })
    }

    fun checkIfUserExists(phone: String, callback : () -> Unit) {
        RetrofitInstance.apiInterface.checkIfUserExists(phone)
            .enqueue(object : Callback<UserExistsResponse> {
                override fun onResponse(
                    call: Call<UserExistsResponse>,
                    response: Response<UserExistsResponse>
                ) {
                    response.body()?.let {
                        userExists = it.user_exists == 1
                    }
                    callback()
                }

                override fun onFailure(call: Call<UserExistsResponse>, t: Throwable) {
                    throw(java.lang.Exception(t.message))
                }
            })
    }
}