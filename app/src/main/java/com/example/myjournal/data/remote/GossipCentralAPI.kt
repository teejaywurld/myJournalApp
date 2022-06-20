package com.example.myjournal.data.remote

import com.example.myjournal.data.model.LoginRequest
import com.example.myjournal.data.model.RegistrationRequest
import com.example.myjournal.data.model.RegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GossipCentralAPI {

    @POST("auth/user")
    suspend fun register(@Body request: RegistrationRequest): RegistrationResponse

    @POST("auth/authenticate")
    suspend fun login(@Body request: LoginRequest): RegistrationResponse
}