package com.shoppitplus.fitlife.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shoppitplus.fitlife.databinding.BottomSheetWorkoutBinding
import com.shoppitplus.fitlife.models.Workout

class WorkoutBottomSheet(private val workout: Workout) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradient = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                0xFF00C896.toInt(),
                0xFF007B5E.toInt()
            )
        ).apply {
            cornerRadii = floatArrayOf(
                24f, 24f,
                24f, 24f,
                0f, 0f,
                0f, 0f
            )
        }

        with(binding) {
            headerBackground.background = gradient
            tvWorkoutName.text = workout.name
            tvWorkoutType.text = workout.type
            tvWorkoutLevel.text = "Level: ${workout.level}"
            tvEquipment.text = "Equipment: ${workout.equipment}"
            tvMuscles.text = "Muscles: ${workout.muscles.joinToString(", ")}"
            tvInstructions.text = workout.instructions
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}