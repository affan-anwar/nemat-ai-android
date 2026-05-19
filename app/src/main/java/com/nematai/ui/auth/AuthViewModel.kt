package com.nematai.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nematai.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthState(error = "Email aur password required hai")
            return
        }
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            val result = repository.login(email.trim(), password.trim())
            _state.value = if (result.isSuccess) {
                AuthState(success = true)
            } else {
                AuthState(error = result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            _state.value = AuthState(error = "Sab fields required hain")
            return
        }
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            val result = repository.register(email.trim(), password.trim(), name.trim())
            _state.value = if (result.isSuccess) {
                AuthState(success = true)
            } else {
                AuthState(error = result.exceptionOrNull()?.message ?: "Register failed")
            }
        }
    }
}
