package com.cesar.mnexpedicoes.features.profile.editprofile.validation

import androidx.core.widget.addTextChangedListener
import com.cesar.mnexpedicoes.databinding.FragmentEditProfileBinding
import com.cesar.mnexpedicoes.utils.setStrokeColorValidation
import com.cesar.mnexpedicoes.utils.toggleAlpha

class InputsValidation() {

    var inputsAreValid = false

    private var nameIsValid = false
    private var phoneIsValid = false
    private var birthdayIsValid = false
    private var cpfIsValid = false

    fun setup(binding: FragmentEditProfileBinding) {
        setupValidations(binding)
    }

    private fun setupValidations(binding: FragmentEditProfileBinding) {
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

        binding.tieBirthday.addTextChangedListener {
            birthdayIsValid = binding.tieBirthday.unMasked.length == 8
            binding.cdvBirthday.setStrokeColorValidation(birthdayIsValid)
            checkInputsValidation(binding)
        }

        binding.tieCpf.addTextChangedListener {
            cpfIsValid = binding.tieCpf.unMasked.length == 11
            binding.cdvCpf.setStrokeColorValidation(cpfIsValid)
            checkInputsValidation(binding)
        }
    }

    private fun checkInputsValidation(binding: FragmentEditProfileBinding) {
        inputsAreValid = !listOf(nameIsValid, phoneIsValid, birthdayIsValid, cpfIsValid).contains(false)
        binding.cdvNext.let { cardView ->
            cardView.toggleAlpha(inputsAreValid)
            cardView.isClickable = inputsAreValid
        }
    }
}