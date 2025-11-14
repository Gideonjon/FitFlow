package com.shoppitplus.fitlife.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoppitplus.fitlife.databinding.ItemWorkoutOptionBinding
import com.shoppitplus.fitlife.models.Workout

class WorkoutPickerAdapter(
    private var workouts: List<Workout>,
    private val onSelect: (Workout) -> Unit
) : RecyclerView.Adapter<WorkoutPickerAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(val binding: ItemWorkoutOptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutOptionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        with(holder.binding) {
            tvWorkoutName.text = workout.name
            tvWorkoutMeta.text = "${workout.type} • ${workout.level}"
            root.setOnClickListener { onSelect(workout) }
        }
    }

    override fun getItemCount(): Int = workouts.size
}