package com.shoppitplus.fitlife.models

data class RawWorkoutResponse(
    val count: Int,
    val user_id: String,
    val workouts: List<List<Any>>
)
