package com.shoppitplus.fitlife.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shoppitplus.fitlife.adapter.WorkoutPickerAdapter
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.BottomSheetWorkoutPickerBinding
import com.shoppitplus.fitlife.models.Workout
import com.shoppitplus.fitlife.models.toWorkout
import kotlinx.coroutines.launch

class WorkoutPickerBottomSheet(
    private val onWorkoutSelected: (Workout) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetWorkoutPickerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WorkoutPickerAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWorkoutPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = WorkoutPickerAdapter(emptyList()) { workout ->
            onWorkoutSelected(workout)
            dismiss()
        }

        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWorkouts.adapter = adapter

        fetchWorkouts()
    }

    private fun fetchWorkouts() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).getWorkouts()

                val workouts = response.workouts.map { it.toWorkout() }

                adapter = WorkoutPickerAdapter(workouts, onWorkoutSelected)
                binding.rvWorkouts.adapter = adapter

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load workouts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}