package com.example.nit3213.ui.screens

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.nit3213.R
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.ui.components.AppScaffold
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailsScreen(
    entityJson: String,
    onBack: () -> Unit,
    gson: Gson = remember { Gson() }
) {
    val entity = remember(entityJson) { gson.fromJson(entityJson, AssetEntity::class.java) }
    val snackbarHost = remember { SnackbarHostState() } // if you want to show errors later

    AppScaffold(title = "Details", onBack = onBack, snackbarHostState = snackbarHost) { padding ->
        AndroidView(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            factory = { ctx ->
                LayoutInflater.from(ctx).inflate(R.layout.view_details, null, false)
            },
            update = { root ->
                bindDetails(root, entity, gson)
            }
        )
    }
}

/* ---------- Binding helpers ---------- */

private fun bindDetails(root: View, e: AssetEntity, gson: Gson) {
    val title = root.findViewById<TextView>(R.id.titleText)
    val typeChip = root.findViewById<Chip>(R.id.typeChip)
    val priceText = root.findViewById<TextView>(R.id.priceText)
    val yieldText = root.findViewById<TextView>(R.id.yieldText)
    val descCard = root.findViewById<MaterialCardView>(R.id.descriptionCard)
    val descText = root.findViewById<TextView>(R.id.descriptionText)
    val rawJsonText = root.findViewById<TextView>(R.id.rawJsonText)

    title.text = e.ticker.orDash()
    typeChip.text = e.assetType.orDash()
    priceText.text = e.currentPrice?.let { currency(it) } ?: "—"
    yieldText.text = e.dividendYield?.let { percent(it) } ?: "—"

    if (!e.description.isNullOrBlank()) {
        descCard.visibility = View.VISIBLE
        descText.text = e.description
    } else {
        descCard.visibility = View.GONE
    }

    // If you want to show raw JSON for debugging, uncomment:
    // rawJsonText.visibility = View.VISIBLE
    // rawJsonText.text = gson.toJson(e)
}

private fun String?.orDash() = if (this.isNullOrBlank()) "—" else this

private fun currency(v: Double): String =
    NumberFormat.getCurrencyInstance(Locale.getDefault()).format(v)

/** If ≤ 1, treat as fraction (0.65 -> 0.65%); otherwise as whole percent (65 -> 65%). */
private fun percent(v: Double): String {
    val p = if (v <= 1.0) v * 100.0 else v
    return String.format(Locale.getDefault(), "%.2f%%", p)
}
