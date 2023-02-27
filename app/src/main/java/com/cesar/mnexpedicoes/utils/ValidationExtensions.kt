package com.cesar.mnexpedicoes.utils

import android.graphics.Typeface
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.cesar.mnexpedicoes.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.santalu.maskara.widget.MaskEditText

fun TextInputEditText.setTextColorValidation2(isValid: Boolean) {
    if (isValid) {
        this.setTextColor(ContextCompat.getColor(this.context, R.color.green_available))
        this.typeface = Typeface.DEFAULT_BOLD
    }
}

fun TextInputEditText.setTextColorValidation(isValid: Boolean) {
    this.addTextChangedListener {
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
        this.typeface = getDefaultTypeface(this.context)
    }
}

fun MaterialCardView.setStrokeColorValidation(isValid: Boolean) {
    if (isValid) {
        this.strokeColor = ContextCompat.getColor(this.context, R.color.green_available)
        this.strokeWidth = 2.dp
    } else {
        this.strokeColor = ContextCompat.getColor(this.context, R.color.red_sold_out)
        this.strokeWidth = 2.dp
    }
}

fun setTextColorValidationOnConfirmPassword(
    password1: TextInputEditText,
    password2: TextInputEditText,
    areValid: Boolean
) {
    val defaultTypeSpace =
        ResourcesCompat.getFont(password1.context, R.font.montserrat_regular) as Typeface

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

fun setStrokeColorValidationOnConfirmPassword(
    password1: MaterialCardView,
    password2: MaterialCardView,
    areValid: Boolean
) {
    password1.strokeWidth = 2.dp
    password2.strokeWidth = 2.dp
    if (areValid) {
        password1.strokeColor = ContextCompat.getColor(password1.context, R.color.green_available)
        password2.strokeColor = ContextCompat.getColor(password1.context, R.color.green_available)
    } else {
        password1.strokeColor = ContextCompat.getColor(password1.context, R.color.red_sold_out)
        password2.strokeColor = ContextCompat.getColor(password1.context, R.color.red_sold_out)
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