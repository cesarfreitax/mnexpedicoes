package com.cesar.mnexpedicoes.fragments.login.register.model

import com.cesar.mnexpedicoes.utils.jsonObjectOf
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserResponse(
    @SerializedName("phone") val phone: String? = "",
    @SerializedName("password") val password: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("birthday") val birthday: String? = "",
    @SerializedName("cpf") val cpf: String? = "",
)

fun UserResponse.toJsonObject() : JsonObject {
    return jsonObjectOf(
        "phone" to this.phone,
        "password" to this.password,
        "name" to this.name,
        "birthday" to this.birthday,
        "cpf" to this.cpf
    )
}


