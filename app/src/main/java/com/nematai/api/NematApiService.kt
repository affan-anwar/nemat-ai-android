package com.nematai.api

import com.nematai.model.AuthResponse
import com.nematai.model.ChatRequest
import com.nematai.model.ChatResponse
import com.nematai.model.LoginRequest
import com.nematai.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NematApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/chat/message")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: ChatRequest
    ): Response<ChatResponse>
}
