package com.cesar.mnexpedicoes.fragments.login.presentation

import androidx.lifecycle.ViewModel
import com.cesar.mnexpedicoes.core.network.RetrofitInstance
import com.cesar.mnexpedicoes.fragments.login.model.CheckCredencialsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragmentViewModel : ViewModel() {

    var loginSuccess = false

    fun checkCredencials(phone: String, password: String, callback : () -> Unit) {
        RetrofitInstance.apiInterface.checkCredencials(phone, password)
            .enqueue(object : Callback<CheckCredencialsResponse> {
                override fun onResponse(
                    call: Call<CheckCredencialsResponse>,
                    response: Response<CheckCredencialsResponse>
                ) {
                    response.body()?.let {
                        loginSuccess = it.user_exists == 1
                    }
                    callback()
                }

                override fun onFailure(call: Call<CheckCredencialsResponse>, t: Throwable) {
                    throw(java.lang.Exception(t.message))
                }
            })
    }

}