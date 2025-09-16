package com.example.nit3213.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nit3213.data.Repository
import com.example.nit3213.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<String>>(UiState.Idle)
    val state: StateFlow<UiState<String>> = _state

    fun login(campus: String, username: String, password: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = repo.login(campus, username.trim(), password.trim())
            _state.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Login failed") }
            )
        }
    }
}
