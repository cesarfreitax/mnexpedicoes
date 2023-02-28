package com.cesar.mnexpedicoes.features.profile.editprofile.model

import com.cesar.mnexpedicoes.utils.jsonObjectOf
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EditProfileUserResponse(
    @SerializedName("phone") val phone: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("birthday") val birthday: String? = "",
    @SerializedName("cpf") val cpf: String? = ""
) : Serializable

fun EditProfileUserResponse.toJsonObject(): JsonObject {
    return jsonObjectOf(
        "phone" to this.phone,
        "name" to this.name,
        "birthday" to this.birthday,
        "cpf" to this.cpf
    )
}


