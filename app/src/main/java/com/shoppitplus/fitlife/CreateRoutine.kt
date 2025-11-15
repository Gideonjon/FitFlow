package com.shoppitplus.fitlife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.FragmentCreateRoutineBinding
import com.shoppitplus.fitlife.models.SaveRoutineRequest
import com.shoppitplus.fitlife.models.Workout
import com.shoppitplus.fitlife.ui.WorkoutPickerBottomSheet
import kotlinx.coroutines.launch


class CreateRoutine : Fragment() {
   private var _binding: FragmentCreateRoutineBinding? = null
    private val binding get() = _binding!!
    private var selectedWorkout: Workout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateRoutineBinding.inflate(inflater, container, false)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val openPicker = {
            val bottomSheet = WorkoutPickerBottomSheet { workout ->
                selectedWorkout = workout
                binding.routineName.setText(workout.name)
                binding.equipment.setText(workout.equipment)
            }
            bottomSheet.show(parentFragmentManager, "WorkoutPicker")
        }

        binding.routineName.setOnClickListener { openPicker() }
        binding.equipment.setOnClickListener { openPicker() }



        binding.btnAddExercise.setOnClickListener {
            saveRoutine()
        }
    }

    private fun saveRoutine() {
        val workout = selectedWorkout
        if (workout == null) {
            Toast.makeText(requireContext(), "Please select a workout", Toast.LENGTH_SHORT).show()
            return
        }

        val description = binding.routineDescription.text.toString().trim()

        val equipmentList = workout.equipment.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val request = SaveRoutineRequest(
            name = workout.name,
            description = description,
            equipment = equipmentList
        )

        lifecycleScope.launch {
            showLoading()

            try {
                val api = RetrofitClient.instance(requireContext())
                val response = api.saveWorkout(workout.id, request)

                hideLoading()

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Routine saved successfully", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Failed to save routine", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                hideLoading()
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error saving routine", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddExercise.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnAddExercise.isEnabled = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}










