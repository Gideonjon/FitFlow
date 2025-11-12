package com.shoppitplus.fitlife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shoppitplus.fitlife.adapter.WorkoutAdapter
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.FragmentHomeScreenBinding
import com.shoppitplus.fitlife.ui.WorkoutBottomSheet
import kotlinx.coroutines.launch


class HomeScreen : Fragment() {
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WorkoutAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)


       /* binding.addRoutine.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_createRoutine)
        }*/
        adapter = WorkoutAdapter(emptyList()) { workout ->
            WorkoutBottomSheet(workout).show(parentFragmentManager, "WorkoutBottomSheet")
        }
        binding.recyclerViewWorkouts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWorkouts.adapter = adapter

        fetchWorkouts()


        return binding.root
    }

    private fun fetchWorkouts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).getWorkouts()
                adapter.updateData(response.workouts)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load workouts", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

