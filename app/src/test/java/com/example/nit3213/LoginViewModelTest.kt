package com.example.nit3213

import com.example.nit3213.data.Repository
import com.example.nit3213.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `login success updates success=true`() = runTest {
        val repo = mockk<Repository>()
        coEvery { repo.login(any(), any(), any()) } returns Result.success("topicName")

        val vm = LoginViewModel(repo)
        vm.login("footscray", "abishek", "4679709")

        val s = vm.state.first { !it.loading }
        assertTrue(s.error == null)
        assertTrue(s.success)
    }

    @Test
    fun `login failure sets error`() = runTest {
        val repo = mockk<Repository>()
        coEvery { repo.login(any(), any(), any()) } returns Result.failure(Exception("bad creds"))

        val vm = LoginViewModel(repo)
        vm.login("sydney", "Wrong", "0000")

        val s = vm.state.first { !it.loading }
        assertFalse(s.success)
        assertTrue(s.error?.contains("bad creds") == true)
    }
}
