package com.shoppitplus.fitlife.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.FragmentCreateRoutineBinding
import com.shoppitplus.fitlife.models.SaveRoutineRequest
import com.shoppitplus.fitlife.models.Workout
import com.shoppitplus.fitlife.ui.WorkoutPickerBottomSheet
import kotlinx.coroutines.launch
import android.app.*
import android.content.Context
import android.content.Intent
import java.util.Calendar

class CreateRoutine : Fragment() {
    private var _binding: FragmentCreateRoutineBinding? = null
    private val binding get() = _binding!!
    private var selectedWorkout: Workout? = null
    private val reminderCalendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateRoutineBinding.inflate(inflater, container, false)

        binding.reminderDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    reminderCalendar.set(year, month, day)
                    binding.reminderDate.setText("$day/${month + 1}/$year")
                },
                reminderCalendar.get(Calendar.YEAR),
                reminderCalendar.get(Calendar.MONTH),
                reminderCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.reminderTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    reminderCalendar.set(Calendar.HOUR_OF_DAY, hour)
                    reminderCalendar.set(Calendar.MINUTE, minute)
                    binding.reminderTime.setText(String.format("%02d:%02d", hour, minute))
                },
                reminderCalendar.get(Calendar.HOUR_OF_DAY),
                reminderCalendar.get(Calendar.MINUTE),
                true
            ).show()
        }

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
                val response =
                    RetrofitClient.instance(requireContext()).saveWorkout(workout.id, request)

                if (!response.isSuccessful) {
                    hideLoading()
                    Toast.makeText(requireContext(), "Failed to save routine", Toast.LENGTH_SHORT)
                        .show()
                    return@launch
                }

                val paymentResponse = RetrofitClient.instance(requireContext())
                    .dummyPayment(mapOf("email" to "an4@gmail.com"))

                if (paymentResponse.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        paymentResponse.body()?.message ?: "Payment successful",
                        Toast.LENGTH_LONG
                    ).show()
                }

                scheduleReminder("Time for your workout: ${workout.name}")
                hideLoading()
                requireActivity().onBackPressedDispatcher.onBackPressed()



                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Routine saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    scheduleReminder("Time for your workout: ${workout.name}")

                } else {
                    Toast.makeText(requireContext(), "Failed to save routine", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                hideLoading()
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error saving routine", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scheduleReminder(message: String) {
        val alarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(requireContext(), ReminderReceiver::class.java).apply {
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(
                    requireContext(),
                    "Please allow exact alarms for reminders",
                    Toast.LENGTH_LONG
                ).show()
                return
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