package com.cesar.mnexpedicoes.utils

import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import java.security.MessageDigest

fun String.hashPassword(): String {
    val bytes = this.toByteArray(Charsets.UTF_8)
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

fun setTextColor(textInputEditText: TextInputEditText, color: Int) {
    textInputEditText.setTextColor(ContextCompat.getColor(textInputEditText.context, color))
}