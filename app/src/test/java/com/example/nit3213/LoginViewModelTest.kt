package com.example.nit3213

import com.example.nit3213.data.Repository
import com.example.nit3213.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `login success updates success state`() = runTest {
        val repo = mockk<Repository>()
        coEvery { repo.login(any(), any(), any()) } returns Result.success("topicName")
        val vm = LoginViewModel(repo)

        vm.login("footscray", "Abishek", "4679709")
        val s = vm.state.first { it !is com.example.nit3213.util.UiState.Loading }

        assertTrue(s is com.example.nit3213.util.UiState.Success)
        assertEquals("topicName", (s as com.example.nit3213.util.UiState.Success).data)
    }

    @Test
    fun `login failure updates error state`() = runTest {
        val repo = mockk<Repository>()
        coEvery { repo.login(any(), any(), any()) } returns Result.failure(IllegalStateException("Bad creds"))
        val vm = LoginViewModel(repo)

        vm.login("footscray", "x", "y")
        val s = vm.state.first { it !is com.example.nit3213.util.UiState.Loading }

        assertTrue(s is com.example.nit3213.util.UiState.Error)
        assertEquals("Bad creds", (s as com.example.nit3213.util.UiState.Error).message)
    }
}
