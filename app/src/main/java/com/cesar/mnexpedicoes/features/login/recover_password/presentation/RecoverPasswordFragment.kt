package com.cesar.mnexpedicoes.features.login.recover_password.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.databinding.FragmentRecoverPasswordBinding
import com.cesar.mnexpedicoes.features.login.recover_password.presentation.confirm_code.presentation.ConfirmCodeFragment
import com.cesar.mnexpedicoes.utils.push
import com.cesar.mnexpedicoes.utils.toggleAlpha

class RecoverPasswordFragment : Fragment() {

    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding: FragmentRecoverPasswordBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
    }

    private fun setupFragment() {
        binding.tiePhone.addTextChangedListener {
            if (binding.tiePhone.unMasked.length >= 11) {
                binding.cdvNext.toggleAlpha(true)
                binding.cdvNext.setOnClickListener {
                    val args = Bundle()
                    args.putString("phone", "+55${binding.tiePhone.unMasked}")
                    push(ConfirmCodeFragment(), args)
                }
            } else {
                binding.cdvNext.toggleAlpha(false)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.cdvNext.toggleAlpha(false)
        (requireActivity() as LoginActivity).apply {
            backBtnVisible = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}