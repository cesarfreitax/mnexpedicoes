package com.cesar.mnexpedicoes.activities.main.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.ActivityMainBinding
import com.cesar.mnexpedicoes.fragments.schedule.presentation.ScheduleFragment
import com.cesar.mnexpedicoes.fragments.home.HomeFragment
import com.cesar.mnexpedicoes.fragments.more.MoreFragment
import com.cesar.mnexpedicoes.utils.toggleVisibility


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val homeFragment = HomeFragment()
        val scheduleFragment = ScheduleFragment()
        val moreFragment = MoreFragment()

        loadFragment(homeFragment)

        binding.bnvMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> loadFragment(homeFragment)
                R.id.schedule -> loadFragment(scheduleFragment)
                R.id.more -> loadFragment(moreFragment)
            }
            true
        }
        backBtnVisible = false

        binding.toolbar.btnBackScreen.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
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
//            binding.toolbar.btnBackScreenBg.toggleVisibility(value)
        }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
            replace(R.id.fcv_fragment_container, fragment)
            commit()
        }

    }

}