package com.example.nit3213.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nit3213.ui.login.LoginViewModel

@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var campus by remember { mutableStateOf("footscray") }

    LaunchedEffect(state.success) { if (state.success) onSuccess() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("NIT3213 Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = campus=="footscray", onClick = { campus = "footscray" })
            Text("Footscray", modifier = Modifier.padding(end = 12.dp))
            RadioButton(selected = campus=="sydney", onClick = { campus = "sydney" })
            Text("Sydney", modifier = Modifier.padding(end = 12.dp))
            RadioButton(selected = campus=="br", onClick = { campus = "br" })
            Text("BR")
        }

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = username, onValueChange = { username = it },
            label = { Text("First name (username)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Student ID (no 's')") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { vm.login(campus, username.trim(), password.trim()) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        ) { Text("Login") }

        if (state.loading) {
            Spacer(Modifier.height(12.dp)); CircularProgressIndicator()
        }
        if (state.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(state.error!!, color = MaterialTheme.colorScheme.error)
        }
    }
}
