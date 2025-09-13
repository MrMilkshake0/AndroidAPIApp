package com.example.nit3213.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nit3213.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun login(campus: String, username: String, password: String) {
        _state.value = LoginUiState(loading = true)
        viewModelScope.launch {
            val result = repo.login(campus, username, password)
            _state.value = result.fold(
                onSuccess = { LoginUiState(success = true) },
                onFailure = { LoginUiState(error = it.message ?: "Login failed") }
            )
        }
    }
}
