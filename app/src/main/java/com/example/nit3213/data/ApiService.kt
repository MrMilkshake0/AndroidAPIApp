package com.example.nit3213.data

import retrofit2.http.*

// ApiService defines the endpoints (paths + HTTP methods) your app can call.
// Retrofit will generate the implementation for this interface at runtime.
interface ApiService {
    // Keep the base URL in one place so the rest of the code can reference it.
    companion object { const val BASE_URL = "https://nit3213api.onrender.com/" }

    // ---- LOGIN ----
    // Example final URLs:
    //   POST https://nit3213api.onrender.com/footscray/auth
    //   POST https://nit3213api.onrender.com/sydney/auth
    //   POST https://nit3213api.onrender.com/br/auth
    // @Path("campus") fills in the {campus} part of the URL.
    // @Body sends a JSON body (LoginRequest).
    @POST("{campus}/auth")
    suspend fun login(
        @Path("campus") campus: String, // "footscray" | "sydney" | "br"
        @Body body: LoginRequest
    ): LoginResponse

    // ---- DASHBOARD ----
    // Example final URL:
    //   GET https://nit3213api.onrender.com/dashboard/{keypass}
    // @Path("keypass") fills in the token from the login step.
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
