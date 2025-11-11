package com.shoppitplus.fitlife.api

import com.shoppitplus.fitlife.utils.LoginRequest
import com.shoppitplus.fitlife.utils.LoginResponse
import com.shoppitplus.fitlife.utils.RegisterRequest
import com.shoppitplus.fitlife.utils.RegistrationResponse
import com.shoppitplus.fitlife.utils.WorkoutItem
import com.shoppitplus.fitlife.utils.WorkoutRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @POST("auth/register")
    fun createAccount(@Body request: RegisterRequest): Call<RegistrationResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("workouts")
    suspend fun getWorkouts(): List<WorkoutItem>

    @POST("user/workouts")
    fun createWorkout(@Body request: WorkoutRequest): Call<LoginResponse>



}