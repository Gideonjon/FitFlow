package com.shoppitplus.fitlife.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.BottomsheetEditWorkoutBinding
import com.shoppitplus.fitlife.models.UserWorkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditWorkoutBottomSheet(
    private val workout: UserWorkout,
    private val onSaved: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetEditWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        _binding = BottomsheetEditWorkoutBinding.inflate(inflater, container, false)

        binding.etName.setText(workout.name)
        binding.etDescription.setText(workout.description)
        binding.etEquipment.setText(workout.equipment.joinToString(","))

        binding.etName.setOnClickListener {
            WorkoutPickerBottomSheet { selected ->
                binding.etName.setText(selected.name)
                binding.etEquipment.setText(selected.equipment)
            }.show(parentFragmentManager, "WorkoutPicker")
        }

        binding.btnSave.setOnClickListener { saveEdit() }

        return binding.root
    }

    private fun saveEdit() {
        val updatedName = binding.etName.text.toString().trim()
        val updatedDescription = binding.etDescription.text.toString().trim()
        val updatedEquip = binding.etEquipment.text.toString().trim()
            .split(",")
            .map { it.trim() }

        val body = mapOf(
            "name" to updatedName,
            "description" to updatedDescription,
            "equipment" to updatedEquip
        )

        CoroutineScope(Dispatchers.Main).launch {
            try {
                RetrofitClient.instance(requireContext())
                    .updateUserWorkout(workout.id, body)

                Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_SHORT).show()
                dismiss()
                onSaved()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
