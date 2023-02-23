package com.cesar.mnexpedicoes.utils

import android.graphics.Typeface
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.cesar.mnexpedicoes.R
import com.google.android.material.textfield.TextInputEditText
import com.santalu.maskara.widget.MaskEditText

fun TextInputEditText.setTextColorValidation(isValid: Boolean) {
    this.addTextChangedListener { text ->
        if (isValid) {
            this.setTextColor(ContextCompat.getColor(this.context, R.color.green_available))
            this.typeface = Typeface.DEFAULT_BOLD
        } else {
            this.setTextColor(ContextCompat.getColor(this.context, R.color.black))
            this.typeface = Typeface.DEFAULT
        }
    }
}

fun MaskEditText.setTextColorValidation(isValid: Boolean) {
    if (isValid) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.green_available))
        this.typeface = Typeface.DEFAULT_BOLD
    } else {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.black))
        this.typeface = Typeface.DEFAULT
    }
}

fun setTextColorValidationOnConfirmPassword(
    password1: TextInputEditText,
    password2: TextInputEditText,
    areValid: Boolean
) {
    val defaultTypeSpace = ResourcesCompat.getFont(password1.context, R.font.montserrat_regular) as Typeface

    if (areValid) {
        setTextColorAndTypeface(password1, R.color.green_available, Typeface.DEFAULT_BOLD)
        setTextColorAndTypeface(password2, R.color.green_available, Typeface.DEFAULT_BOLD)
    } else {
        setTextColorAndTypeface(password1, R.color.black, defaultTypeSpace)
        setTextColorAndTypeface(password2, R.color.black, defaultTypeSpace)
    }
    if (areValid) {
        setTextColorAndTypeface(password1, R.color.green_available, Typeface.DEFAULT_BOLD)
        setTextColorAndTypeface(password2, R.color.green_available, Typeface.DEFAULT_BOLD)
    } else {
        setTextColorAndTypeface(password1, R.color.black, defaultTypeSpace)
        setTextColorAndTypeface(password2, R.color.black, defaultTypeSpace)
    }
}


private fun setTextColorAndTypeface(
    view: TextInputEditText,
    color: Int,
    typeface: Typeface
) {
    view.setTextColor(ContextCompat.getColor(view.context, color))
    view.typeface = typeface
}

fun CardView.toggleAlpha(isActive: Boolean) {
    if (isActive) {
        this.alpha = 1f
    } else {
        this.alpha = 0.5f
    }
}

fun TextInputEditText.setTextColorSuccess() {
    this.setTextColor(ContextCompat.getColor(this.context, R.color.green_available))
}

fun TextInputEditText.setTextColorError() {
    this.setTextColor(ContextCompat.getColor(this.context, R.color.red_sold_out))
}