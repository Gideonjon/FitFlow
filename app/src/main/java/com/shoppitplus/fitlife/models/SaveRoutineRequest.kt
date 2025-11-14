package com.shoppitplus.fitlife.models

data class SaveRoutineRequest(
    val name: String,
    val description: String,
    val equipment: List<String>
)