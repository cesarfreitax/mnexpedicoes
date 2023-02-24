package com.cesar.mnexpedicoes.fragments.login.register.validation

import androidx.core.widget.addTextChangedListener
import com.cesar.mnexpedicoes.databinding.FragmentRegisterBinding
import com.cesar.mnexpedicoes.utils.setTextColorValidation
import com.cesar.mnexpedicoes.utils.setTextColorValidationOnConfirmPassword
import com.cesar.mnexpedicoes.utils.toggleAlpha

class InputsValidation() {

    var inputsAreValid = false
    private var nameIsValid = false
    private var phoneIsValid = false
    private var passwordIsValid = false

    fun setup(binding: FragmentRegisterBinding) {
        setupValidations(binding)
    }

    private fun setupValidations(binding: FragmentRegisterBinding) {
        binding.tieName.addTextChangedListener {
            nameIsValid = it.toString().length > 3
            binding.tieName.setTextColorValidation(nameIsValid)
            checkInputsValidation(binding)
        }

        binding.tiePhone.addTextChangedListener {
            phoneIsValid = binding.tiePhone.unMasked.length == 11
            binding.tiePhone.setTextColorValidation(phoneIsValid)
            checkInputsValidation(binding)
        }

        binding.tiePassword.addTextChangedListener {
            passwordIsValid = it.toString() == binding.tieConfirmPassword.text.toString() && it.toString().length > 3
            setTextColorValidationOnConfirmPassword(
                binding.tiePassword,
                binding.tieConfirmPassword,
                passwordIsValid
            )
            checkInputsValidation(binding)
        }

        binding.tieConfirmPassword.addTextChangedListener {
            passwordIsValid = it.toString() == binding.tiePassword.text.toString() && it.toString().length > 3
            setTextColorValidationOnConfirmPassword(
                binding.tiePassword,
                binding.tieConfirmPassword,
                passwordIsValid
            )
            checkInputsValidation(binding)
        }
    }

    private fun checkInputsValidation(binding: FragmentRegisterBinding) {
        inputsAreValid = !listOf(nameIsValid, phoneIsValid, passwordIsValid).contains(false)
        binding.cdvNext.let { cardView ->
            cardView.toggleAlpha(inputsAreValid)
            cardView.isClickable = inputsAreValid
        }
    }
}