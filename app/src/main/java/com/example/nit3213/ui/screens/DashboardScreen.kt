// app/src/main/java/com/example/nit3213/ui/screens/DashboardScreen.kt
package com.example.nit3213.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.ui.components.AppScaffold
import com.example.nit3213.ui.dashboard.DashboardViewModel
import com.google.gson.Gson
import androidx.compose.ui.Alignment
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Composable
fun DashboardScreen(
    onOpen: (json: String) -> Unit,
    vm: DashboardViewModel = hiltViewModel(),
    gson: Gson = remember { Gson() }
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { vm.load() }
    LaunchedEffect(state.error) { state.error?.let { snackbar.showSnackbar(it) } }

    AppScaffold(title = "Dashboard", snackbarHostState = snackbar) { padding ->
        when {
            state.loading -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.items.isEmpty() && state.error == null -> Text(
                "No entities.",
                modifier = Modifier.padding(padding).padding(16.dp)
            )

            else -> AndroidView(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                factory = { ctx ->
                    RecyclerView(ctx).apply {
                        layoutManager = LinearLayoutManager(ctx)
                        adapter = AssetAdapter { e: AssetEntity ->
                            onOpen(gson.toJson(e))   // navigate with entity JSON
                        }
                    }
                },
                update = { rv ->
                    @Suppress("UNCHECKED_CAST")
                    (rv.adapter as AssetAdapter).submitList(state.items)
                }
            )
        }
    }
}
