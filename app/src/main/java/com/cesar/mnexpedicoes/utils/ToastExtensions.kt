package com.cesar.mnexpedicoes.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.makeToast(text: String) {
    Toast.makeText(this.requireContext(), text, Toast.LENGTH_LONG).show()
}
