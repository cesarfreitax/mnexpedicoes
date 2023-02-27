package com.cesar.mnexpedicoes.features.profile.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cesar.mnexpedicoes.activities.main.presentation.MainActivity
import com.cesar.mnexpedicoes.databinding.FragmentTicketsBinding

class TicketsFragment : Fragment() {

    private var _binding: FragmentTicketsBinding? = null
    private val binding: FragmentTicketsBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).apply {
            bottomBarHidden = true
            backBtnVisible = true
        }
    }
}