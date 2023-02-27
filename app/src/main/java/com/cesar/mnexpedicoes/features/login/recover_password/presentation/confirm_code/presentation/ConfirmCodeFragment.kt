package com.cesar.mnexpedicoes.features.login.recover_password.presentation.confirm_code.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.databinding.FragmentConfirmCodeBinding
import com.cesar.mnexpedicoes.utils.makeToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ConfirmCodeFragment : Fragment() {

    private var _binding: FragmentConfirmCodeBinding? = null
    private val binding: FragmentConfirmCodeBinding
        get() = requireNotNull(_binding)
    private var phone = ""
    private var code = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phone = arguments?.getString("phone").toString()
        setupFragment()
    }

    private fun setupFragment() {
        println(phone)
        val phoneAuthProvider = PhoneAuthProvider.getInstance()
        phoneAuthProvider.verifyPhoneNumber(
            phone,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credencial: PhoneAuthCredential) {
                    code = credencial.smsCode.toString()
                    println(code)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("VerificationFailed", e.message, e)
                    makeToast("Houve algum erro ao gerar o código.")
                }

            }
        )

        binding.tieCodeVerify.addTextChangedListener {
            if (it.toString() == code) {
                makeToast("Código validado com sucesso!")
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