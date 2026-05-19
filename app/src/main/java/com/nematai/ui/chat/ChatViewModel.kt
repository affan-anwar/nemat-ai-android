package com.nematai.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nematai.model.ChatMessage
import com.nematai.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val userName: String = "Affan",
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state

    private var token: String? = null
    private var userId: String? = null

    init {
        viewModelScope.launch {
            combine(repository.tokenFlow, repository.userIdFlow, repository.nameFlow)
            { t, u, n -> Triple(t, u, n) }.collect { (t, u, n) ->
                token = t
                userId = u
                _state.update { it.copy(userName = n ?: "Affan") }
                if (_state.value.messages.isEmpty()) {
                    _state.update {
                        it.copy(messages = listOf(
                            ChatMessage(
                                content = "Assalamu Alaikum ${n ?: ""}! Main nemat.ai hun — Md Affan Anwar ka AI assistant. Kuch bhi puchho! 🌟",
                                isUser = false
                            )
                        ))
                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return
        val t = token ?: return
        val u = userId ?: return

        _state.update {
            it.copy(messages = it.messages + ChatMessage(content = message, isUser = true),
                isLoading = true, error = null)
        }

        viewModelScope.launch {
            val result = repository.sendMessage(t, u, message)
            if (result.isSuccess) {
                val reply = result.getOrNull()?.reply ?: "Koi response nahi mila"
                _state.update {
                    it.copy(messages = it.messages + ChatMessage(content = reply, isUser = false),
                        isLoading = false)
                }
            } else {
                _state.update {
                    it.copy(isLoading = false,
                        error = result.exceptionOrNull()?.message)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }
}
