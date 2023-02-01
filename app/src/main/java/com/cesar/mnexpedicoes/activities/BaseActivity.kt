package com.cesar.mnexpedicoes.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.databinding.ActivityBaseBinding
import com.cesar.mnexpedicoes.fragments.events.EventsFragment
import com.cesar.mnexpedicoes.fragments.home.HomeFragment
import com.cesar.mnexpedicoes.fragments.more.MoreFragment

class BaseActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBaseBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val homeFragment = HomeFragment()
        val eventsFragment = EventsFragment()
        val moreFragment = MoreFragment()

        loadFragment(homeFragment)

        binding.bnvMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> loadFragment(homeFragment)
                R.id.events -> loadFragment(eventsFragment)
                R.id.more -> loadFragment(moreFragment)
            }
            true
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_fragment_container, fragment)
            .commit()
    }
}