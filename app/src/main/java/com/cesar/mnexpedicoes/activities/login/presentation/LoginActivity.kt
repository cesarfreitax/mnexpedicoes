package com.cesar.mnexpedicoes.activities.login.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cesar.mnexpedicoes.databinding.ActivityLoginBinding
import com.cesar.mnexpedicoes.features.login.presentation.LoginFragment
import com.cesar.mnexpedicoes.utils.push
import com.cesar.mnexpedicoes.utils.toggleVisibility

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding
        get() = requireNotNull(_binding)
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActivity()
    }

    private fun setupActivity() {
        push(loginFragment)
        setupToolbar()
    }

    private fun setupToolbar() {
        backBtnVisible = false
        binding.btnBackScreen.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }

    var backBtnVisible = false
        set(value) {
            field = value
            binding.btnBackScreen.toggleVisibility(value)
        }
}