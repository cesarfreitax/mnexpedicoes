package com.cesar.mnexpedicoes.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity

fun AppCompatActivity.loadFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction().apply {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(
            R.id.fcv_fragment_container,
            fragment
        )
        commit()
    }
}

fun Fragment.push(fragmentDestination: Fragment, args: Bundle? = null) {
    val transaction = parentFragmentManager.beginTransaction()
    transaction.addToBackStack(null)
    args?.let {
        val bundle = Bundle(args)
        fragmentDestination.arguments = bundle
    }
    transaction.replace(R.id.fcv_fragment_container, fragmentDestination).commit()
}

fun Fragment.navigateToActivity(activity: AppCompatActivity) {
    val intent = Intent(requireActivity(), activity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)

}
