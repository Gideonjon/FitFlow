package com.shoppitplus.fitlife.utils

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
