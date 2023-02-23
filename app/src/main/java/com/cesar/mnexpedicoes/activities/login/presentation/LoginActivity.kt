package com.cesar.mnexpedicoes.activities.login.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cesar.mnexpedicoes.databinding.ActivityLoginBinding
import com.cesar.mnexpedicoes.fragments.login.presentation.LoginFragment
import com.cesar.mnexpedicoes.utils.loadFragment
import com.cesar.mnexpedicoes.utils.toggleVisibility

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActivity()
    }

    private fun setupActivity() {
        loadFragment(loginFragment)
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