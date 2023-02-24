package com.cesar.mnexpedicoes.fragments.login.presentation

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.FragmentLoginBinding
import com.cesar.mnexpedicoes.fragments.home.presentation.HomeFragment
import com.cesar.mnexpedicoes.fragments.login.recover_password.presentation.RecoverPasswordFragment
import com.cesar.mnexpedicoes.fragments.login.register.presentation.RegisterFragment
import com.cesar.mnexpedicoes.utils.*

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        setupFragment()
    }

    private fun setupFragment() {
        setCreateAccountBtn()
        setLoginBtn()
        setupTogglePasswordVisibility()
        binding.txtForgottenPassword.setOnClickListener {
            push(RecoverPasswordFragment())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTogglePasswordVisibility() {
        binding.txtPasswordVisibilityToggle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                binding.txtPasswordVisibilityToggle.compoundDrawables[0].setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pink
                    )
                )
                binding.txtPasswordVisibilityToggle.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                binding.txtPasswordVisibilityToggle.typeface = Typeface.DEFAULT_BOLD
                binding.tiePassword.transformationMethod = null
            } else if (event.actionMasked == MotionEvent.ACTION_UP) {
                binding.txtPasswordVisibilityToggle.compoundDrawables[0].setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                binding.txtPasswordVisibilityToggle.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
                binding.txtPasswordVisibilityToggle.typeface = Typeface.DEFAULT
                binding.tiePassword.transformationMethod = PasswordTransformationMethod()
            }
            true
        }
    }

    private fun setLoginBtn() {
        binding.cdvLoginBtn.setOnClickListener {
            setProgress()
            loginHandler()
        }
    }

    private fun loginHandler() {
        viewModel.checkCredencials(
            phone = binding.tiePhone.unMasked,
            password = binding.tiePassword.text.toString().hashPassword()
        ) {
            if (viewModel.loginSuccess) {
                navigateToHome()
            } else {
                this.makeToastError("Dados incorretos! Tente novamente.")
                setProgress()
            }
        }
    }

    private fun navigateToHome() {
        this.navigateToActivity(MainActivity())
    }

    private fun setCreateAccountBtn() {
        binding.lnrCreateAcc.setOnClickListener {
            push(RegisterFragment())
        }
    }

    private fun setProgress() {
        binding.cdvLoginBtn.isClickable = !binding.cdvLoginBtn.isClickable
        binding.imgNext.toggleVisibility()
        binding.pgbLoading.toggleVisibility()
    }

    override fun onStart() {
        super.onStart()
        val activity = requireActivity()
        if (activity is LoginActivity) {
            activity.apply {
                backBtnVisible = false
            }
        }
    }

}