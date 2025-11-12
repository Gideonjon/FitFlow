package com.shoppitplus.fitlife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shoppitplus.fitlife.databinding.FragmentExpenseBinding

class Expense : Fragment() {
    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }


}