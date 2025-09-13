package com.example.nit3213

import com.example.nit3213.data.DashboardResponse
import com.example.nit3213.data.Repository
import com.example.nit3213.ui.dashboard.DashboardViewModel
import com.google.gson.JsonObject
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `load emits items on success`() = runTest {
        val repo = mockk<Repository>()
        val e = JsonObject().apply {
            addProperty("property1", "value1"); addProperty("description", "desc")
        }
        coEvery { repo.getDashboard() } returns Result.success(DashboardResponse(listOf(e), 1))

        val vm = DashboardViewModel(repo)
        vm.load()

        val s = vm.state.first { !it.loading }
        assertTrue(s.error == null)
        assertEquals(1, s.items.size)
    }

    @Test
    fun `load emits error on failure`() = runTest {
        val repo = mockk<Repository>()
        coEvery { repo.getDashboard() } returns Result.failure(IllegalStateException("boom"))

        val vm = DashboardViewModel(repo)
        vm.load()

        val s = vm.state.first { !it.loading }
        assertTrue(s.items.isEmpty())
        assertTrue(s.error?.contains("boom") == true)
    }
}
