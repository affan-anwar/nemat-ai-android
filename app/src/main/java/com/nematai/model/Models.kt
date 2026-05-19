package com.nematai.model

data class RegisterRequest(val email: String, val password: String, val name: String)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val token: String, val userId: String, val name: String, val email: String)
data class ChatRequest(val message: String, val userId: String)
data class ChatResponse(val reply: String, val role: String, val timestamp: Long)
data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
