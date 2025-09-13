package com.example.nit3213.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val api: ApiService
) {
    @Volatile var keypass: String? = null
        private set

    suspend fun login(campus: String, username: String, password: String): Result<String> =
        runCatching {
            val res = api.login(campus, LoginRequest(username, password))
            keypass = res.keypass
            res.keypass
        }

    suspend fun getDashboard(): Result<DashboardResponse> =
        runCatching {
            val kp = keypass ?: error("Missing keypass. Please login first.")
            api.getDashboard(kp)
        }
}
