package com.example.nit3213.data

import javax.inject.Inject
import javax.inject.Singleton

// Repository is the single place that talks to the network (ApiService).
// ViewModels call Repository functions; Repository returns Result<T> to indicate success/failure.
// This keeps networking details out of your Activities/ViewModels.
@Singleton
class Repository @Inject constructor(
    private val api: ApiService
) {
    // In-memory storage for the keypass returned by login.
    // Note: this will be lost if the app is killed. For persistence,
    // consider DataStore/SharedPreferences.
    @Volatile var keypass: String? = null
        private set

    // Perform login. On success:
    // - store the keypass for later
    // - return Result.success(keypass)
    // On failure:
    // - wrap the exception inside Result.failure
    suspend fun login(campus: String, username: String, password: String): Result<String> =
        runCatching {
            val res = api.login(campus, LoginRequest(username, password))
            keypass = res.keypass
            res.keypass
        }

    // Fetch dashboard data using the stored keypass.
    // If keypass is missing (e.g., user hasn’t logged in), we throw an error
    // which becomes Result.failure for the caller to handle.
    suspend fun getDashboard(): Result<DashboardResponse> =
        runCatching {
            val kp = keypass ?: error("Missing keypass. Please login first.")
            api.getDashboard(kp)
        }
}
