package com.example.nit3213.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nit3213.ui.components.AppScaffold
import com.example.nit3213.ui.login.LoginViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues


@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var campus by remember { mutableStateOf("footscray") }
    val campuses = listOf("footscray", "sydney", "br")

    LaunchedEffect(state.success) { if (state.success) onSuccess() }
    LaunchedEffect(state.error) { state.error?.let { snackbar.showSnackbar(it) } }

    AppScaffold(title = "NIT3213 Login", snackbarHostState = snackbar) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Campus segmented buttons
            SingleChoiceSegmentedButtonRow {
                campuses.forEachIndexed { idx, option ->
                    SegmentedButton(
                        selected = campus == option,
                        onClick = { campus = option },
                        shape = SegmentedButtonDefaults.itemShape(index = idx, count = campuses.size),
                        label = { Text(option.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = username, onValueChange = { username = it },
                label = { Text("First name (username)") },
                leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Student ID (no 's')") },
                leadingIcon = { Icon(Icons.Rounded.Badge, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = { vm.login(campus, username.trim(), password.trim()) },
                enabled = !state.loading,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContent(targetState = state.loading, label = "login-loading") { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Login")
                    }
                }
            }
        }
    }
}
