package com.shoppitplus.fitlife.api

import com.shoppitplus.fitlife.models.LoginRequest
import com.shoppitplus.fitlife.models.LoginResponse
import com.shoppitplus.fitlife.models.RegisterRequest
import com.shoppitplus.fitlife.models.RegistrationResponse
import com.shoppitplus.fitlife.models.WorkoutRequest
import com.shoppitplus.fitlife.models.WorkoutResponse
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

    @POST("user/workouts")
    fun createWorkout(@Body request: WorkoutRequest): Call<LoginResponse>

    @GET("public/workouts")
    suspend fun getWorkouts(): WorkoutResponse

}