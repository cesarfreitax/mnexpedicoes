package com.cesar.mnexpedicoes.fragments.login.register.password

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.FragmentRegisterBinding

class TogglePasswordAction(private val binding: FragmentRegisterBinding) {

    init {
        setupTogglePasswordVisibility()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTogglePasswordVisibility() {
        binding.txtPasswordVisibilityToggle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                binding.txtPasswordVisibilityToggle.compoundDrawables[0].setTint(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.pink
                    )
                )
                binding.txtPasswordVisibilityToggle.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
                binding.txtPasswordVisibilityToggle.typeface = Typeface.DEFAULT_BOLD
                binding.tiePassword.transformationMethod = null
                binding.tieConfirmPassword.transformationMethod = null
            } else if (event.actionMasked == MotionEvent.ACTION_UP) {
                binding.txtPasswordVisibilityToggle.compoundDrawables[0].setTint(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
                binding.txtPasswordVisibilityToggle.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.grey
                    )
                )
                binding.txtPasswordVisibilityToggle.typeface = Typeface.DEFAULT
                binding.tiePassword.transformationMethod = PasswordTransformationMethod()
                binding.tieConfirmPassword.transformationMethod = PasswordTransformationMethod()
            }
            true
        }
    }
}