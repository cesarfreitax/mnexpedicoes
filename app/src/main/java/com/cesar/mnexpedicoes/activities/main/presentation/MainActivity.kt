package com.cesar.mnexpedicoes.activities.main.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.ActivityMainBinding
import com.cesar.mnexpedicoes.features.home.presentation.HomeFragment
import com.cesar.mnexpedicoes.features.profile.presentation.ProfileFragment
import com.cesar.mnexpedicoes.features.schedule.presentation.ScheduleFragment
import com.cesar.mnexpedicoes.utils.getSharedPreferences
import com.cesar.mnexpedicoes.utils.push
import com.cesar.mnexpedicoes.utils.toggleVisibility

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = requireNotNull(_binding)
    private val fragments = listOf(HomeFragment(), ScheduleFragment(), ProfileFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setupActivity()
    }

    private fun setupActivity() {
        saveUserId()
        setupNavigation()
        setupToolbar()
    }

    private fun setupNavigation() {
        push(fragments[0])
        binding.bnvMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> push(fragments[0])
                R.id.schedule -> push(fragments[1])
                R.id.profile -> push(fragments[2])
            }
            true
        }
    }

    private fun setupToolbar() {
        backBtnVisible = false
        binding.toolbar.btnBackScreen.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }

    private fun saveUserId() {
        val userId = intent.getStringExtra("data")
        val sharedPref = getSharedPreferences()
        sharedPref.edit().putString("userId", userId).apply()
    }

    var bottomBarHidden = false
        set(value) {
            field = value
            binding.bnvMenu.toggleVisibility(!value)
        }

    var backBtnVisible = false
        set(value) {
            field = value
            binding.toolbar.btnBackScreen.toggleVisibility(value)
        }
}