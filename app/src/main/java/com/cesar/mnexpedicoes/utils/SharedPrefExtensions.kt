package com.cesar.mnexpedicoes.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.getSharedPreferences() = activity?.getSharedPreferences("prefs", Context.MODE_PRIVATE)

fun AppCompatActivity.getSharedPreferences(): SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)