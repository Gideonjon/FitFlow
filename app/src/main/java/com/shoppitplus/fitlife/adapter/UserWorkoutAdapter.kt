package com.shoppitplus.fitlife.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoppitplus.fitlife.databinding.ItemUserWorkoutBinding
import com.shoppitplus.fitlife.models.UserWorkout

class UserWorkoutAdapter(
    private var list: MutableList<UserWorkout>,
    private val onItemChecked: (UserWorkout, Boolean) -> Unit,
    private val onEditClick: (UserWorkout) -> Unit,
    private val onDeleteSwipe: (UserWorkout) -> Unit
) : RecyclerView.Adapter<UserWorkoutAdapter.ViewHolder>() {

    private val checkedState = hashMapOf<Int, Boolean>()

    inner class ViewHolder(val binding: ItemUserWorkoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserWorkoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = list[position]

        with(holder.binding) {
            tvWorkoutName.text = workout.name
            tvWorkoutDescription.text = workout.description
            tvWorkoutEquipment.text = "Equipment: ${workout.equipment.joinToString(", ")}"

            checkBoxSelect.isChecked = checkedState[workout.id] ?: false

            root.setOnClickListener {
                val newState = !(checkedState[workout.id] ?: false)
                checkedState[workout.id] = newState
                checkBoxSelect.isChecked = newState
                onItemChecked(workout, newState)
            }

            checkBoxSelect.setOnCheckedChangeListener { _, isChecked ->
                checkedState[workout.id] = isChecked
                onItemChecked(workout, isChecked)
            }

            btnEdit.setOnClickListener { onEditClick(workout) }
        }
    }

    override fun getItemCount() = list.size

    fun update(newList: List<UserWorkout>) {
        list = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        val workout = list[position]
        onDeleteSwipe(workout)
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}
