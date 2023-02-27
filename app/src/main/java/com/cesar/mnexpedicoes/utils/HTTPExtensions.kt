package com.cesar.mnexpedicoes.utils

import com.cesar.mnexpedicoes.features.login.register.model.UserResponse
import com.google.gson.Gson
import com.google.gson.JsonObject

fun jsonObjectOf(vararg pairs: Pair<String, Any?>): JsonObject {
    val jsonObject = JsonObject()
    for (pair in pairs) {
        val value = pair.second
        if (value != null) {
            when (value) {
                is Boolean -> jsonObject.addProperty(pair.first, value)
                is Number -> jsonObject.addProperty(pair.first, value)
                is String -> jsonObject.addProperty(pair.first, value)
                is JsonObject -> jsonObject.add(pair.first, value)
                else -> throw IllegalArgumentException("Unsupported type: ${value.javaClass}")
            }
        }
    }
    return jsonObject
}

fun toJsonObject(user: UserResponse) : JsonObject = Gson().toJsonTree(user).asJsonObject