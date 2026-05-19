package com.nematai.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nematai.model.ChatMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onLogout: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto scroll
    LaunchedEffect(state.messages.size) {
        val lastIndex = state.messages.lastIndex
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "N",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text("nemat.ai", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(
                                "by Md Affan Anwar",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },

                actions = {
                    TextButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Text("Exit")
                    }
                }
            )
        },

        bottomBar = {
            Surface(shadowElevation = 8.dp) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Ask something...") },
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3
                    )

                    Spacer(Modifier.width(8.dp))

                    FilledIconButton(
                        onClick = {
                            val msg = inputText.trim()
                            if (msg.isNotEmpty()) {
                                viewModel.sendMessage(msg)
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank() && !state.isLoading,
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send"
                        )
                    }
                }
            }
        }

    ) { padding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(state.messages) { message ->
                MessageBubble(message)
            }

            if (state.isLoading) {
                item {
                    TypingIndicator()
                }
            }

            val errorMsg = state.error
            if (errorMsg != null) {
                item {
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            if (message.isUser) Arrangement.End else Arrangement.Start
    ) {

        if (!message.isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text("N", color = Color.White, fontSize = 12.sp)
            }

            Spacer(Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (message.isUser)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text("N", color = Color.White, fontSize = 12.sp)
        }

        Spacer(Modifier.width(8.dp))

        CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            strokeWidth = 2.dp
        )
    }
}
