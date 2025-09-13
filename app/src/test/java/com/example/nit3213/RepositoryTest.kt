package com.example.nit3213

import com.example.nit3213.data.*
import com.google.gson.JsonObject
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RepositoryTest {

    @Test
    fun `login then dashboard succeeds`() = runTest {
        val api = mockk<ApiService>()

        // When logging in, return keypass "topicX"
        coEvery { api.login("footscray", any()) } returns LoginResponse("topicX")

        // When dashboard called with that keypass, return one entity
        val entity = JsonObject().apply { addProperty("property1", "value1") }
        coEvery { api.getDashboard("topicX") } returns DashboardResponse(listOf(entity), 1)

        val repo = Repository(api)

        val loginRes = repo.login("footscray", "abishek", "4679709")
        assertTrue(loginRes.isSuccess)

        val dashRes = repo.getDashboard()
        assertTrue(dashRes.isSuccess)
        assertEquals(1, dashRes.getOrNull()?.entities?.size)
    }
}
