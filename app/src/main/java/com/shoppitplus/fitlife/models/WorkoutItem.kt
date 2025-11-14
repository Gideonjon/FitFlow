package com.shoppitplus.fitlife.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

data class WorkoutItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("equipment") val equipment: List<String>,
    @SerializedName("checklist") val checklist: List<ChecklistItem>
)

data class ChecklistItem(
    @SerializedName("id") val id: Int,
    @SerializedName("task") val task: String,
    @SerializedName("done") val done: Boolean
)

@Keep
data class WorkoutResponse(
    @SerializedName("count")
    val count: Int,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("workouts")
    val workouts: List<Workout>
)

@Keep
data class Workout(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("level")
    val level: String,

    @SerializedName("equipment")
    val equipment: String,

    @SerializedName("instructions")
    val instructions: String,

    @SerializedName("muscles")
    val muscles: List<String>
)

data class UserWorkout(
    val id: Int,
    val name: String,
    val description: String,
    val equipment: List<String>
)
