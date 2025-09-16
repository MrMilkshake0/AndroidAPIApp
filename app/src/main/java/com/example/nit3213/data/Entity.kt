package com.example.nit3213.data

import com.google.gson.JsonObject

// AssetEntity is a clean Kotlin model your UI can easily display.
// (It’s mapped from raw JSON coming from the API.)
data class AssetEntity(
    val assetType: String,
    val ticker: String,
    val currentPrice: Double?,
    val dividendYield: Double?,
    val description: String?
) {
    companion object {
        // Helper to convert a JSON object into an AssetEntity.
        // We use safe calls and fallbacks so bad/missing fields don’t crash the app.
        fun fromJson(obj: JsonObject) = AssetEntity(
            assetType      = obj.get("assetType")?.asString ?: "Unknown",
            ticker         = obj.get("ticker")?.asString ?: "—",
            currentPrice   = obj.get("currentPrice")?.asDoubleOrNull(),
            dividendYield  = obj.get("dividendYield")?.asDoubleOrNull(),
            description    = obj.get("description")?.asString
        )
    }
}

// Small extension that tries to read a Double from a JsonElement,
// and returns null if it can’t (instead of throwing).
private fun com.google.gson.JsonElement.asDoubleOrNull(): Double? =
    runCatching { asDouble }.getOrNull()
