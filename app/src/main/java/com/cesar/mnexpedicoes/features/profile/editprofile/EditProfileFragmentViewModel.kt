package com.cesar.mnexpedicoes.features.profile.editprofile

import androidx.lifecycle.ViewModel
import com.cesar.mnexpedicoes.core.network.RetrofitInstance
import com.cesar.mnexpedicoes.features.profile.editprofile.model.EditProfileUserResponse
import com.cesar.mnexpedicoes.features.profile.editprofile.model.toJsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileFragmentViewModel : ViewModel() {

    private var _userUpdatedSuccessfull = false
    val userUpdatedSuccessfull: Boolean
        get() = _userUpdatedSuccessfull

    fun updateUserByPhone(
        userResponse: EditProfileUserResponse,
        callback: () -> Unit
    ) {
        RetrofitInstance.apiInterface.updateUserByPhone(
            userResponse.phone.toString(),
            userResponse.toJsonObject()
        )
            .enqueue(object : Callback<EditProfileUserResponse> {
                override fun onResponse(
                    call: Call<EditProfileUserResponse>,
                    response: Response<EditProfileUserResponse>
                ) {
                    _userUpdatedSuccessfull = response.isSuccessful
                    callback()
                }

                override fun onFailure(call: Call<EditProfileUserResponse>, t: Throwable) {
                    throw (Exception(t.message))
                }
            })
    }
}