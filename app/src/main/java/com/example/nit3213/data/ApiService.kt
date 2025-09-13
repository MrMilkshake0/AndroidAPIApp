package com.example.nit3213.data

import retrofit2.http.*

interface ApiService {
    companion object { const val BASE_URL = "https://nit3213api.onrender.com/" }

    @POST("{campus}/auth")
    suspend fun login(
        @Path("campus") campus: String, // "footscray" | "sydney" | "br"
        @Body body: LoginRequest
    ): LoginResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
