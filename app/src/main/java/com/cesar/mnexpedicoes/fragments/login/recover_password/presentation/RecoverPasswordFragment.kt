package com.cesar.mnexpedicoes.fragments.login.recover_password.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.databinding.FragmentRecoverPasswordBinding
import com.cesar.mnexpedicoes.fragments.login.recover_password.presentation.confirm_code.presentation.ConfirmCodeFragment
import com.cesar.mnexpedicoes.utils.push

class RecoverPasswordFragment : Fragment() {

    private lateinit var binding: FragmentRecoverPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recover_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecoverPasswordBinding.bind(view)
        setupFragment()
    }

    private fun setupFragment() {
        val inputPhone = binding.tiePhone.unMasked
        binding.tiePhone.addTextChangedListener {
            if (inputPhone.length == 11) {
                val args = Bundle()
                args.putString("phone", inputPhone)
                push(ConfirmCodeFragment(), args)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as LoginActivity).apply {
            backBtnVisible = true
        }
    }
}