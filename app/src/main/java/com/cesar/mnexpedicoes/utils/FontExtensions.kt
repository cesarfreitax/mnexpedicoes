package com.cesar.mnexpedicoes.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R

fun Fragment.getDefaultTypeface(): Typeface =
    ResourcesCompat.getFont(this.requireContext(), R.font.montserrat_regular) as Typeface

fun getDefaultTypeface(context: Context): Typeface =
    ResourcesCompat.getFont(context, R.font.montserrat_regular) as Typeface
