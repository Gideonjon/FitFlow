package com.shoppitplus.fitlife.api

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://backend-3sys.onrender.com/"

    private const val TAG = "RetrofitClient"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var retrofit: Retrofit? = null

    fun instance(context: Context): Api {
        if (retrofit == null) {
            synchronized(this) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getOkHttpClient(context))
                        .build()
                }
            }
        }
        return retrofit!!.create(Api::class.java)
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(getAuthInterceptor(context))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    }

    private fun getAuthInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val authToken = getAuthToken(context)

            val contentType = request.header("Content-Type")

            val modifiedRequest = if (authToken.isNotEmpty()) {
                Log.d("RetrofitClient", "Request URL: ${request.url}")

                val requestBuilder = request.newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")


                if (contentType == null) {
                    requestBuilder.addHeader("Accept", "application/json")

                }

                requestBuilder.build()
            } else {
                Log.w("RetrofitClient", "No auth token found!")
                request
            }

            chain.proceed(modifiedRequest)
        }
    }


    private fun getAuthToken(context: Context): String {
        val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        val possibleKeys = listOf(
            "auth_token",
            "token",
            "access_token",
            "jwt",
            "user_token"
        )

        for (key in possibleKeys) {
            val t = prefs.getString(key, null)
            if (!t.isNullOrEmpty()) {
                Log.d("AuthInterceptor", "Token found using key: $key -> $t")
                return t
            }
        }

        Log.w("AuthInterceptor", "No token found in SharedPreferences")
        return ""
    }




}