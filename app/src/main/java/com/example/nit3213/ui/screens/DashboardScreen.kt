package com.example.nit3213.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nit3213.ui.dashboard.DashboardViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject

@Composable
fun DashboardScreen(
    onOpen: (json: String) -> Unit,
    vm: DashboardViewModel = hiltViewModel(),
    gson: Gson = remember { Gson() }
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.load() }

    Column(Modifier.fillMaxSize()) {
        Text(
            "Dashboard",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        when {
            state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
            state.error != null -> Text(
                state.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            state.items.isEmpty() -> Text(
                "No entities.",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.items) { obj ->
                    EntityCard(obj) { onOpen(gson.toJson(obj)) }
                }
            }
        }
    }
}

@Composable
private fun EntityCard(obj: JsonObject, onClick: () -> Unit) {
    val summary = remember(obj) { summaryFrom(obj) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(summary, modifier = Modifier.padding(16.dp))
    }
}

private fun summaryFrom(o: JsonObject): String {
    val pairs = mutableListOf<String>()
    for ((k, v) in o.entrySet()) {
        if (k.equals("description", true)) continue
        pairs += "$k: ${v.asNiceString()}"
        if (pairs.size >= 2) break
    }
    return pairs.takeIf { it.isNotEmpty() }?.joinToString(" • ") ?: "(No summary)"
}

private fun JsonElement.asNiceString(): String =
    if (isJsonPrimitive) asJsonPrimitive.toString().trim('"') else toString()
