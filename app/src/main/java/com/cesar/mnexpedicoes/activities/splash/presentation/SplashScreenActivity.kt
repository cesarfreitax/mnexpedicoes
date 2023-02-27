package com.cesar.mnexpedicoes.activities.splash.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.databinding.SplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: SplashScreenBinding? = null
    private val binding: SplashScreenBinding
        get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActivity()
    }

    private fun setupActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToMainActivity()
        }, 3000)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}