package com.cesar.mnexpedicoes.features.login.register.validation

import androidx.core.widget.addTextChangedListener
import com.cesar.mnexpedicoes.databinding.FragmentRegisterBinding
import com.cesar.mnexpedicoes.utils.*

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
            binding.cdvName.setStrokeColorValidation(nameIsValid)
            checkInputsValidation(binding)
        }

        binding.tiePhone.addTextChangedListener {
            phoneIsValid = binding.tiePhone.unMasked.length == 11
            binding.cdvPhone.setStrokeColorValidation(phoneIsValid)
            checkInputsValidation(binding)
        }

        binding.tiePassword.addTextChangedListener {
            passwordIsValid = it.toString() == binding.tieConfirmPassword.text.toString() && it.toString().length > 3
            setStrokeColorValidationOnConfirmPassword(
                binding.cdvPassword,
                binding.cdvConfirmPassword,
                passwordIsValid
            )
            checkInputsValidation(binding)
        }

        binding.tieConfirmPassword.addTextChangedListener {
            passwordIsValid = it.toString() == binding.tiePassword.text.toString() && it.toString().length > 3
            setStrokeColorValidationOnConfirmPassword(
                binding.cdvConfirmPassword,
                binding.cdvPassword,
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