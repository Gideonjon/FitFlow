package com.shoppitplus.fitlife.api

import com.shoppitplus.fitlife.models.LoginRequest
import com.shoppitplus.fitlife.models.LoginResponse
import com.shoppitplus.fitlife.models.RegisterRequest
import com.shoppitplus.fitlife.models.RegistrationResponse
import com.shoppitplus.fitlife.models.SaveRoutineRequest
import com.shoppitplus.fitlife.models.ToggleResponse
import com.shoppitplus.fitlife.models.UserWorkout
import com.shoppitplus.fitlife.models.WorkoutRequest
import com.shoppitplus.fitlife.models.WorkoutResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @POST("auth/register")
    fun createAccount(@Body request: RegisterRequest): Call<RegistrationResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("public/workouts")
    suspend fun getWorkouts(): WorkoutResponse

    @POST("public/workouts/save/{id}")
    suspend fun saveWorkout(
        @Path("id") id: Int,
        @Body request: SaveRoutineRequest
    ): Response<Unit>

    @GET("users/workouts")
    suspend fun getUserWorkouts(): List<UserWorkout>

    @PATCH("users/checklist/{ids}")
    suspend fun toggleChecklist(
        @Path("ids") ids: String
    ): ToggleResponse


}