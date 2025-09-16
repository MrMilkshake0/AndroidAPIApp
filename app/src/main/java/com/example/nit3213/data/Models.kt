package com.example.nit3213.data

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

// These are the raw network models that match the API JSON exactly.

// Body sent to the login endpoint.
// {
//   "username": "...",
//   "password": "..."
// }
data class LoginRequest(
    val username: String,
    val password: String
)

// Response returned from the login endpoint.
// {
//   "keypass": "topicName"
// }
data class LoginResponse(
    // Use @SerializedName to be explicit about mapping JSON → property.
    @SerializedName("keypass") val keypass: String
)

// Response returned from the dashboard endpoint.
// {
//   "entities": [ { ... }, { ... } ],
//   "entityTotal": 7
// }
// We keep entities as a List<JsonObject> here so we can map it ourselves
// to AssetEntity in the ViewModel (via AssetEntity.fromJson).
data class DashboardResponse(
    val entities: List<JsonObject>,
    val entityTotal: Int
)
