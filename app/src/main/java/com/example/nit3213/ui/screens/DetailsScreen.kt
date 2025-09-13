package com.example.nit3213.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonObject

@Composable
fun DetailsScreen(entityJson: String, gson: Gson = remember { Gson() }) {
    val obj = remember(entityJson) { gson.fromJson(entityJson, JsonObject::class.java) }
    val pretty = remember(obj) { gson.toJson(obj) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
    ) {
        Text(
            "Details",
            style = MaterialTheme.typography.titleLarge,
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        )
        Text(
            pretty,
            fontFamily = FontFamily.Monospace,
            modifier = androidx.compose.ui.Modifier
                .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp))
        )
    }
}
