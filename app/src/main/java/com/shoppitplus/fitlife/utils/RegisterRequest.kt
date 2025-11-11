package com.shoppitplus.fitlife.utils

import com.google.gson.annotations.SerializedName


data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("reg_number") val regNumber: String,
    @SerializedName("email") val email: String
)
data class RegistrationResponse(
    @SerializedName("message") val message: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("error") val error: String
)

data class WorkoutRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("equipment") val equipment: List<String>
)