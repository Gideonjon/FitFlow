package com.shoppitplus.fitlife

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shoppitplus.fitlife.adapter.UserWorkoutAdapter
import com.shoppitplus.fitlife.adapter.WorkoutAdapter
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.FragmentHomeScreenBinding
import com.shoppitplus.fitlife.models.Workout
import com.shoppitplus.fitlife.ui.WorkoutBottomSheet
import kotlinx.coroutines.launch


class HomeScreen : Fragment() {
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WorkoutAdapter
    private var allWorkouts = listOf<Workout>()
    private lateinit var userWorkoutAdapter: UserWorkoutAdapter
    private var selectedWorkouts = mutableListOf<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)


        binding.addRoutine.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_createRoutine)
        }
        adapter = WorkoutAdapter(emptyList()) { workout ->
            WorkoutBottomSheet(workout).show(parentFragmentManager, "WorkoutBottomSheet")
        }
        binding.recyclerViewWorkouts.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        binding.recyclerViewWorkouts.adapter = adapter

        binding.etSearch.addTextChangedListener {
            filterWorkouts(it.toString())
        }

        userWorkoutAdapter = UserWorkoutAdapter(emptyList()) { workout, checked ->
            if (checked) {
                toggleAndRemove(listOf(workout.id))
            }
        }


        binding.recyclerViewMyWorkouts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewMyWorkouts.adapter = userWorkoutAdapter

        fetchUserWorkouts()


        fetchWorkouts()


        return binding.root
    }

    private fun fetchWorkouts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).getWorkouts()

                // Save the list for searching
                allWorkouts = response.workouts

                // Update adapter
                adapter.updateData(allWorkouts)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load workouts", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun fetchUserWorkouts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).getUserWorkouts()
                userWorkoutAdapter.update(response)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to load your workouts", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun filterWorkouts(query: String) {
        val q = query.trim().lowercase()

        if (q.isEmpty()) {
            adapter.updateData(allWorkouts)
            return
        }

        val filtered = allWorkouts.filter { workout ->

            // name
            workout.name.lowercase().contains(q) ||

                    // type
                    workout.type.lowercase().contains(q) ||

                    // level
                    workout.level.lowercase().contains(q) ||

                    // equipment (string)
                    workout.equipment.lowercase().contains(q) ||

                    // instructions
                    workout.instructions.lowercase().contains(q) ||

                    // muscles (list)
                    workout.muscles.any { it.lowercase().contains(q) }
        }

        adapter.updateData(filtered)
    }

    private fun toggleAndRemove(ids: List<Int>) {
        lifecycleScope.launch {
            try {
                val idString = ids.joinToString(",")
                val response = RetrofitClient.instance(requireContext()).toggleChecklist(idString)

                val removedIds = response.toggled_items.map { it.id }

                userWorkoutAdapter.removeItems(removedIds)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to update checklist", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

