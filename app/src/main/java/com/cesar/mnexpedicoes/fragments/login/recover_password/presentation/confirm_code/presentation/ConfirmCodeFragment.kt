package com.cesar.mnexpedicoes.fragments.login.recover_password.presentation.confirm_code.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.activities.login.presentation.LoginActivity
import com.cesar.mnexpedicoes.databinding.FragmentConfirmCodeBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.*
import java.util.concurrent.TimeUnit

class ConfirmCodeFragment : Fragment() {

    private lateinit var binding: FragmentConfirmCodeBinding
    private var phone = ""
    private var code = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConfirmCodeBinding.bind(view)
        this.phone = arguments?.getString("phone").toString()
        setupFragment()
    }

    private fun setupFragment() {
        FirebaseApp.initializeApp(requireContext())
        val auth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    code = credential.smsCode.toString()
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    TODO("Not yet implemented")
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    binding.tieCodeVerify.addTextChangedListener {
                        if (it.toString() == code) {
                            Toast.makeText(requireContext(), "OK!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }).build()
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.SEND_SMS),
                1
            )
        } else {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage("+55$phoneNumber", null, message, null, null)
        }
    }

    private fun generateRandomCode(): String {
        val random = Random()
        val code = random.nextInt(999999 - 100000) + 100000
        return code.toString()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as LoginActivity).apply {
            backBtnVisible = true
        }
    }
}