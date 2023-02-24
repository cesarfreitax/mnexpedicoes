package com.cesar.mnexpedicoes.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R

fun Fragment.makeToastSuccess(text: String) {
    val toast = Toast.makeText(this.requireContext(), text, Toast.LENGTH_LONG)
    val shape = GradientDrawable()
    shape.cornerRadius = 30.dp.toFloat()
    shape.setColor(
        ContextCompat.getColor(
            this.requireContext(),
            R.color.green_available
        )
    )
    val toastTxt = toast.view?.findViewById(android.R.id.message) as TextView
    toastTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    toastTxt.typeface = Typeface.DEFAULT_BOLD
    toast.view?.background = shape
    toast.show()
}

fun Fragment.makeToastError(text: String) {
    val toast = Toast.makeText(this.requireContext(), text, Toast.LENGTH_LONG)
    val shape = GradientDrawable()
    shape.cornerRadius = 30.dp.toFloat()
    shape.setColor(
        ContextCompat.getColor(
            this.requireContext(),
            R.color.red_sold_out
        )
    )
    val toastTxt = toast.view?.findViewById(android.R.id.message) as TextView
    toastTxt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    toastTxt.typeface = Typeface.DEFAULT_BOLD
    toast.view?.background = shape
    toast.show()
}
