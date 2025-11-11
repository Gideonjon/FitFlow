package com.shoppitplus.fitlife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shoppitplus.fitlife.databinding.FragmentHomeScreenBinding


class HomeScreen : Fragment() {
   private var _binding : FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeScreenBinding.inflate(inflater,container,false)


        binding.addRoutine.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_createRoutine)
        }

        binding.expense.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_expense2)
        }

        binding.routine.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_excercise)
        }

        return binding.root
    }


}