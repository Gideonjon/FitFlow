package com.shoppitplus.fitlife.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoppitplus.fitlife.databinding.ItemWorkoutBinding
import com.shoppitplus.fitlife.models.Workout


class WorkoutAdapter(
    private var workouts: List<Workout>,
    private val onItemClick: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        with(holder.binding) {
            tvWorkoutName.text = workout.name
            tvWorkoutType.text = workout.type
            tvWorkoutLevel.text = "Level: ${workout.level}"
            tvEquipment.text = "Equipment: ${workout.equipment}"
            tvMuscles.text = "Muscles: ${workout.muscles.joinToString(", ")}"
            tvInstructions.text = workout.instructions.take(100) + "..." // preview only
            root.setOnClickListener { onItemClick(workout) }
        }
    }

    override fun getItemCount(): Int = workouts.size

    fun updateData(newList: List<Workout>) {
        workouts = newList
        notifyDataSetChanged()
    }
}
