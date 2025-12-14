package com.shoppitplus.fitlife.models

fun List<Any>.toWorkout(): Workout {
    return Workout(
        id = (this[0] as Double).toInt(),
        name = this[1] as String,
        equipment = this[2] as String,
        type = this[3] as String,
        muscles = this[4] as List<String>,
        level = this[5] as String,
        instructions = this[6] as String
    )
}
