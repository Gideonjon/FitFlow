package com.shoppitplus.fitlife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shoppitplus.fitlife.databinding.FragmentCreateRoutineBinding


class CreateRoutine : Fragment() {
   private var _binding: FragmentCreateRoutineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }


}