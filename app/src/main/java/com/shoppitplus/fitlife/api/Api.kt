package com.shoppitplus.fitlife.api

import com.shoppitplus.fitlife.models.LoginRequest
import com.shoppitplus.fitlife.models.LoginResponse
import com.shoppitplus.fitlife.models.PaymentResponse
import com.shoppitplus.fitlife.models.RawWorkoutResponse
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
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {

    @POST("auth/register")
    fun createAccount(@Body request: RegisterRequest): Call<RegistrationResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("public/workouts")
    suspend fun getWorkouts(): RawWorkoutResponse

    @POST("public/workouts/save/{id}")
    suspend fun saveWorkout(
        @Path("id") id: Int,
        @Body request: SaveRoutineRequest
    ): Response<Unit>

    @GET("users/workouts")
    suspend fun getUserWorkouts(): List<UserWorkout>

    @PATCH("users/checklist/{id}")
    suspend fun toggleChecklist(
        @Path("id") id: Int
    ): ToggleResponse


    @DELETE("users/workouts/{id}")
    suspend fun deleteUserWorkout(
        @Path("id") id: Int
    ): Response<Unit>

    @PUT("users/workouts/{id}")
    suspend fun updateUserWorkout(
        @Path("id") id: Int,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<Unit>

    @POST("users/paystack/dummy-payment")
    suspend fun dummyPayment(@Body body: Map<String, String>): Response<PaymentResponse>


}