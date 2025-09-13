package com.example.nit3213.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.ui.components.AppScaffold
import com.google.gson.Gson
import java.util.*

@Composable
fun DetailsScreen(
    entityJson: String,
    onBack: () -> Unit,
    gson: Gson = remember { Gson() }
) {
    val entity = remember(entityJson) { gson.fromJson(entityJson, AssetEntity::class.java) }

    AppScaffold(title = "Details", onBack = onBack) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Text(entity.ticker, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(6.dp))
            AssistChip(
                onClick = {},
                label = { Text(entity.assetType) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )

            Spacer(Modifier.height(16.dp))

            // Key info grid
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                ),
            ) {
                Column(Modifier.padding(16.dp)) {
                    KeyRow("Current price", entity.currentPrice?.let { currency(it) } ?: "—")
                    KeyRow("Dividend yield", entity.dividendYield?.let { percent(it) } ?: "—")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Description
            if (!entity.description.isNullOrBlank()) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Description", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(entity.description!!, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Raw JSON (optional, useful for marking/debug)
            /*Spacer(Modifier.height(16.dp))
            Text(gson.toJson(entity), fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))*/
        }
    }
}

@Composable private fun KeyRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun currency(v: Double): String =
    java.text.NumberFormat.getCurrencyInstance(Locale.getDefault()).format(v)

private fun percent(v: Double): String {
    val p = if (v <= 1.0) v * 100.0 else v
    return String.format(Locale.getDefault(), "%.2f%%", p)
}
