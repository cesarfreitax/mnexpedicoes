package com.cesar.mnexpedicoes.features.profile.presentation

import androidx.lifecycle.ViewModel
import com.cesar.mnexpedicoes.core.network.RetrofitInstance
import com.cesar.mnexpedicoes.features.login.register.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragmentViewModel: ViewModel() {

    private var _user: UserResponse? = null
    val user: UserResponse
        get() = requireNotNull(_user)

    fun getUser(phone: String, callback: () -> Unit) {
        RetrofitInstance.apiInterface.getUserByPhone(phone)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    response.body()?.let {
                        _user = UserResponse(
                            phone = it.phone,
                            password = it.password,
                            name = it.name,
                            birthday = it.birthday,
                            cpf = it.cpf,
                            photo = it.photo
                        )
                    }
                    callback()
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                }
            })
    }
}