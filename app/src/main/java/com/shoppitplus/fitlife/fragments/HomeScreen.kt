package com.shoppitplus.fitlife.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shoppitplus.fitlife.R
import com.shoppitplus.fitlife.adapter.UserWorkoutAdapter
import com.shoppitplus.fitlife.adapter.WorkoutAdapter
import com.shoppitplus.fitlife.api.RetrofitClient
import com.shoppitplus.fitlife.databinding.FragmentHomeScreenBinding
import com.shoppitplus.fitlife.models.UserWorkout
import com.shoppitplus.fitlife.models.Workout
import com.shoppitplus.fitlife.ui.EditWorkoutBottomSheet
import com.shoppitplus.fitlife.ui.WorkoutBottomSheet
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeScreen : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var workoutAdapter: WorkoutAdapter
    private lateinit var userWorkoutAdapter: UserWorkoutAdapter

    private var allWorkouts = listOf<Workout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        setupHeader()
        setupWorkoutList()
        setupMyWorkoutList()
        setupSearch()

        fetchWorkouts()
        fetchUserWorkouts()

        binding.addRoutine.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_createRoutine)
        }

        binding.btnShareApp.setOnClickListener { shareApp() }

        return binding.root
    }

    private fun setupHeader() {
        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = prefs.getString("user_name", "") ?: ""

        binding.userName.text = "${getGreetingMessage()}, $username 👋"
    }

    private fun setupWorkoutList() {
        workoutAdapter = WorkoutAdapter(emptyList()) { workout ->
            WorkoutBottomSheet(workout).show(parentFragmentManager, "WorkoutBottomSheet")
        }

        binding.recyclerViewWorkouts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = workoutAdapter
        }
    }

    private fun setupMyWorkoutList() {

        userWorkoutAdapter = UserWorkoutAdapter(
            mutableListOf(),
            onItemChecked = { workout, checked ->
                if (checked) toggleChecklist(workout.id)
            },
            onEditClick = { workout ->
                EditWorkoutBottomSheet(workout) {
                    fetchUserWorkouts()
                }.show(parentFragmentManager, "EditWorkout")
            },
            onDeleteSwipe = { workout ->
                deleteWorkout(workout.id)
            }
        )

        binding.recyclerViewMyWorkouts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userWorkoutAdapter
        }

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                val position = vh.adapterPosition
                userWorkoutAdapter.removeAt(position)
            }
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.recyclerViewMyWorkouts)
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener {
            filterWorkouts(it.toString())
        }
    }

    private fun getGreetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    private fun fetchWorkouts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).getWorkouts()
                allWorkouts = response.workouts
                workoutAdapter.updateData(allWorkouts)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load workouts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserWorkouts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).getUserWorkouts()
                userWorkoutAdapter.update(response)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load your workouts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterWorkouts(query: String) {
        val q = query.lowercase().trim()

        val filtered = if (q.isEmpty()) {
            allWorkouts
        } else {
            allWorkouts.filter { workout ->
                workout.name.lowercase().contains(q) ||
                        workout.type.lowercase().contains(q) ||
                        workout.level.lowercase().contains(q) ||
                        workout.equipment.lowercase().contains(q) ||
                        workout.instructions.lowercase().contains(q) ||
                        workout.muscles.any { it.lowercase().contains(q) }
            }
        }

        workoutAdapter.updateData(filtered)
    }

    private fun toggleChecklist(id: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(requireContext()).toggleChecklist(id)

                // Backend returns: {"message":"no items found"}
                if (response.message == "no items found") {
                    Toast.makeText(requireContext(), "Checklist item does not exist", Toast.LENGTH_SHORT).show()
                } else {
                    fetchUserWorkouts()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Checklist update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun deleteWorkout(id: Int) {
        lifecycleScope.launch {
            try {
                RetrofitClient.instance(requireContext()).deleteUserWorkout(id)
                fetchUserWorkouts()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareApp() {
        val appLink = "https://drive.google.com/drive/folders/1DZZ_SZzrjUKk_xwcilodiShjerevDfjx?usp=sharing"

        val shareText = """
        Hey! Check out this fitness app I'm using.
        Download here:
        $appLink
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share App")
        startActivity(shareIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
