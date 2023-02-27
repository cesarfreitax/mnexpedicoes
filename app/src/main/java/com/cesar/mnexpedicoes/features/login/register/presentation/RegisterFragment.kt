package com.cesar.mnexpedicoes.features.login.register.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.FragmentRegisterBinding
import com.cesar.mnexpedicoes.features.login.register.model.UserResponse
import com.cesar.mnexpedicoes.features.login.register.password.TogglePasswordAction
import com.cesar.mnexpedicoes.features.login.register.validation.InputsValidation
import com.cesar.mnexpedicoes.utils.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = requireNotNull(_binding)
    private val viewModel: RegisterFragmentViewModel by viewModels()
    private val inputsValidation = InputsValidation()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        inputsValidation.setup(binding)
        TogglePasswordAction(binding)
        setupRegister()
    }


    private fun setupRegister() {
        binding.cdvNext.toggleAlpha(false)
        binding.cdvNext.setOnClickListener {
            if (inputsValidation.inputsAreValid) {
                setProgress()
                checkIfUserExists()
            }

        }

    }

    private fun checkIfUserExists() {
        viewModel.checkIfUserExists(phone = binding.tiePhone.unMasked) {
            if (viewModel.userExists) showError() else postUser()
        }
    }

    private fun showError() {
        setProgress()
        binding.tiePhone.setTextColorSuccess()
        this.makeToast(getString(R.string.error_register_phone))
    }

    private fun postUser() {
        val user = UserResponse(
            phone = binding.tiePhone.unMasked,
            password = binding.tiePassword.text.toString().hashPassword(),
            name = binding.tieName.text.toString()
        )
        viewModel.postUser(user) {
            setProgress()
            if (viewModel.userCreatedSuccessfull) {
                this.makeToast("Conta criada com sucesso!")
                navigateToHome()
            } else {
                this.makeToast(getString(R.string.default_error))
            }
        }
    }

    private fun navigateToHome() {
        navigateToActivity(MainActivity())
    }

    private fun setProgress() {
        binding.imgNext.toggleVisibility()
        binding.pgbLoading.toggleVisibility()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as LoginActivity).apply {
            backBtnVisible = true
        }
    }
}