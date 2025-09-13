package com.example.nit3213.data

import com.google.gson.JsonObject

data class AssetEntity(
    val assetType: String,
    val ticker: String,
    val currentPrice: Double?,
    val dividendYield: Double?,
    val description: String?
) {
    companion object {
        fun fromJson(obj: JsonObject) = AssetEntity(
            assetType      = obj.get("assetType")?.asString ?: "Unknown",
            ticker         = obj.get("ticker")?.asString ?: "—",
            currentPrice   = obj.get("currentPrice")?.asDoubleOrNull(),
            dividendYield  = obj.get("dividendYield")?.asDoubleOrNull(),
            description    = obj.get("description")?.asString
        )
    }
}

private fun com.google.gson.JsonElement.asDoubleOrNull(): Double? =
    runCatching { asDouble }.getOrNull()