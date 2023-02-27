package com.cesar.mnexpedicoes.features.login.register.model

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
    @SerializedName("photo") val photo: String? = ""
) : Serializable

fun UserResponse.toJsonObject() : JsonObject {
    return jsonObjectOf(
        "phone" to this.phone,
        "password" to this.password,
        "name" to this.name,
        "birthday" to this.birthday,
        "cpf" to this.cpf,
        "photo" to this.phone
    )
}


