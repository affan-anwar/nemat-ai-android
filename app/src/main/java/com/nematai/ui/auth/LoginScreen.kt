package com.nematai.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var isLoginTab by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showForgotPassword by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }

    LaunchedEffect(state.success) {
        if (state.success) onLoginSuccess()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "nemat.ai",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7C6FCD)
                )
                Text(
                    text = "by Md Affan Anwar",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(24.dp))

                // Tabs
                Row(modifier = Modifier.fillMaxWidth()) {
                    TabButton("Login", isLoginTab) { isLoginTab = true }
                    Spacer(Modifier.width(8.dp))
                    TabButton("Sign Up", !isLoginTab) { isLoginTab = false }
                }

                Spacer(Modifier.height(16.dp))

                if (showForgotPassword) {
                    // Forgot Password UI
                    Text("Reset Password", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = forgotEmail,
                        onValueChange = { forgotEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { showForgotPassword = false },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Send Reset Link") }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { showForgotPassword = false }) {
                        Text("Back to Login", color = Color(0xFF7C6FCD))
                    }
                } else if (isLoginTab) {
                    // Login UI
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    TextButton(
                        onClick = { showForgotPassword = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Forgot Password?", color = Color(0xFF7C6FCD), fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    state.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                    }
                    Button(
                        onClick = { viewModel.login(email.trim(), password.trim()) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        else Text("Login", fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Register UI
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = mobile,
                        onValueChange = { mobile = it },
                        label = { Text("Mobile Number") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    state.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                        Spacer(Modifier.height(8.dp))
                    }
                    Button(
                        onClick = {
                            if (password == confirmPassword) {
                                viewModel.register(email.trim(), password.trim(), name.trim())
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !state.isLoading,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        else Text("Sign Up", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF7C6FCD) else Color(0xFF26215C)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text, color = Color.White, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}
