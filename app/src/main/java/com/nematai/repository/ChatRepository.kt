package com.nematai.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nematai.api.NematApiService
import com.nematai.model.AuthResponse
import com.nematai.model.ChatRequest
import com.nematai.model.ChatResponse
import com.nematai.model.LoginRequest
import com.nematai.model.RegisterRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nemat_prefs")

@Singleton
class ChatRepository @Inject constructor(
    private val api: NematApiService,
    @ApplicationContext private val context: Context
) {
    private val TOKEN_KEY  = stringPreferencesKey("token")
    private val USERID_KEY = stringPreferencesKey("userId")
    private val NAME_KEY   = stringPreferencesKey("name")

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val userIdFlow: Flow<String?> = context.dataStore.data.map { it[USERID_KEY] }
    val nameFlow: Flow<String?> = context.dataStore.data.map { it[NAME_KEY] }

    suspend fun register(email: String, password: String, name: String): Result<AuthResponse> {
        return try {
            val res = api.register(RegisterRequest(email, password, name))
            if (res.isSuccessful && res.body() != null) {
                saveAuth(res.body()!!)
                Result.success(res.body()!!)
            } else {
                Result.failure(Exception("Registration failed: ${res.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val res = api.login(LoginRequest(email, password))
            if (res.isSuccessful && res.body() != null) {
                saveAuth(res.body()!!)
                Result.success(res.body()!!)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    suspend fun sendMessage(token: String, userId: String, message: String): Result<ChatResponse> {
        return try {
            val res = api.sendMessage("Bearer $token", ChatRequest(message, userId))
            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                Result.failure(Exception("Chat error: ${res.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    private suspend fun saveAuth(auth: AuthResponse) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY]  = auth.token
            prefs[USERID_KEY] = auth.userId
            prefs[NAME_KEY]   = auth.name
        }
    }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }
}
