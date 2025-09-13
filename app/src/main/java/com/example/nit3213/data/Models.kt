package com.example.nit3213.data

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(@SerializedName("keypass") val keypass: String)
data class DashboardResponse(val entities: List<JsonObject>, val entityTotal: Int)
