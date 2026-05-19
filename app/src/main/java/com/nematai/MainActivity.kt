package com.nematai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nematai.repository.ChatRepository
import com.nematai.ui.auth.LoginScreen
import com.nematai.ui.chat.ChatScreen
import com.nematai.ui.theme.NematTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: ChatRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NematTheme {
                val token by repository.tokenFlow.collectAsState(initial = null)
                val startDest = if (token != null) "chat" else "login"
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = startDest
                ) {
                    composable("login") {
                        LoginScreen(onLoginSuccess = {
                            navController.navigate("chat") {
                                popUpTo("login") { inclusive = true }
                            }
                        })
                    }
                    composable("chat") {
                        ChatScreen(onLogout = {
                            navController.navigate("login") {
                                popUpTo("chat") { inclusive = true }
                            }
                        })
                    }
                }
            }
        }
    }
}
