package com.example.nit3213.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.ui.components.AppScaffold
import com.example.nit3213.ui.dashboard.DashboardViewModel
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.*

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
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) { CircularProgressIndicator() }

            state.items.isEmpty() && state.error == null -> Text(
                "No entities.",
                modifier = Modifier.padding(padding).padding(16.dp)
            )

            else -> LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.items) { e ->
                    AssetCard(entity = e) { onOpen(gson.toJson(e)) }
                }
            }
        }
    }
}

@Composable
private fun AssetCard(entity: AssetEntity, onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            // Header: Ticker + chevron
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(
                    text = entity.ticker,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(Modifier.height(6.dp))

            // AssetType chip
            AssistChip(
                onClick = {},
                label = { Text(entity.assetType) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )

            Spacer(Modifier.height(8.dp))

            // Secondary info row
            val price = entity.currentPrice?.let { currency(it) } ?: "—"
            val yield = entity.dividendYield?.let { percent(it) } ?: "—"
            Text(
                text = "Price: $price • Yield: $yield",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )
        }
    }
}

private fun currency(v: Double): String =
    NumberFormat.getCurrencyInstance(Locale.getDefault()).format(v)

/* Some APIs send 0.65 to mean 0.65% (not 65). Heuristic:
   if value <= 1, treat as fraction; else treat as whole percent. */
private fun percent(v: Double): String {
    val p = if (v <= 1.0) v * 100.0 else v
    return String.format(Locale.getDefault(), "%.2f%%", p)
}
