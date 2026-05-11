package com.hazelgym.mobile.data.remote

import com.hazelgym.mobile.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiClient {
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(httpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val attendanceApi: AttendanceApi = retrofit.create(AttendanceApi::class.java)
    val gymClassApi: GymClassApi = retrofit.create(GymClassApi::class.java)
    val machineApi: MachineApi = retrofit.create(MachineApi::class.java)
    val routineAssignmentApi: RoutineAssignmentApi = retrofit.create(RoutineAssignmentApi::class.java)
    val routineApi: RoutineApi = retrofit.create(RoutineApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
}
