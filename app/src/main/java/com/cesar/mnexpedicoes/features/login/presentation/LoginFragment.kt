package com.cesar.mnexpedicoes.features.login.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.FragmentLoginBinding
import com.cesar.mnexpedicoes.features.login.recover_password.presentation.RecoverPasswordFragment
import com.cesar.mnexpedicoes.features.login.register.presentation.RegisterFragment
import com.cesar.mnexpedicoes.utils.*

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = requireNotNull(_binding)
    private val viewModel: LoginFragmentViewModel by viewModels()
    private var sharedPref: SharedPreferences? = null
    private var keepLogin = false
    private var phoneSharedPref: String? = null
    private var togglePasswordIsActive = false
    private var defaultPasswordType: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        defaultPasswordType = binding.tiePassword.inputType
        checkKeepLogin()
        setCreateAccountBtn()
        setLoginBtn()
        setupTogglePasswordVisibility()
        setupRecoverPassword()
    }

    private fun checkKeepLogin() {
        sharedPref = getSharedPreferences()
        phoneSharedPref = sharedPref?.getString("phone", "")
        if (!phoneSharedPref.isNullOrBlank()) {
            keepLogin = true
            navigateToHome()
        }
    }

    private fun setupRecoverPassword() {
        binding.txtForgottenPassword.setOnClickListener {
            push(RecoverPasswordFragment())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTogglePasswordVisibility() {
        binding.txtPasswordVisibilityToggle.setOnClickListener {
            togglePasswordIsActive = !togglePasswordIsActive
            if (togglePasswordIsActive) {
                binding.txtPasswordVisibilityToggle.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                binding.txtPasswordVisibilityToggle.typeface = Typeface.DEFAULT_BOLD
                binding.txtPasswordVisibilityToggle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_eye, 0, 0, 0)
                binding.tiePassword.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                binding.txtPasswordVisibilityToggle.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                binding.txtPasswordVisibilityToggle.typeface = getDefaultTypeface()
                binding.txtPasswordVisibilityToggle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_off, 0, 0, 0)
                binding.tiePassword.inputType = defaultPasswordType!!
            }
            binding.tiePassword.setSelection(binding.tiePassword.text?.length ?: 0)
            binding.tiePassword.textSize = resources.getDimension(R.dimen.txt_password_1)
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
                if (binding.ckbKeepLogin.isChecked) {
                    setKeepLogin()
                }
                navigateToHome()
            } else {
                this.makeToast("Dados incorretos! Tente novamente.")
                setProgress()
            }
        }
    }

    private fun setKeepLogin() {
        sharedPref?.edit()?.putString("phone", binding.tiePhone.unMasked)?.apply()
    }

    private fun navigateToHome() {
        if (keepLogin) {
            navigateToActivity(MainActivity(), phoneSharedPref)
        } else {
            navigateToActivity(MainActivity(), binding.tiePhone.unMasked)
        }

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