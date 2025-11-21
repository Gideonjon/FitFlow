package com.shoppitplus.fitlife.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shoppitplus.fitlife.R
import com.shoppitplus.fitlife.databinding.FragmentOnboardingScreenBinding

class OnboardingScreen : Fragment() {
    private var _binding: FragmentOnboardingScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnboardingScreenBinding.inflate(inflater, container, false)

        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingScreen_to_signUp)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingScreen_to_login)
        }




        return binding.root
    }


}