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

// ViewModel = "brain" for the LoginActivity.
// It survives rotation changes, holds login state, and talks to the Repository.
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: Repository // Repository handles the actual network calls
) : ViewModel() {

    // MutableStateFlow holds the current login state (Idle, Loading, Success, Error).
    private val _state = MutableStateFlow<UiState<String>>(UiState.Idle)

    // Expose an immutable version so Activities/Fragments can only "read", not change it.
    val state: StateFlow<UiState<String>> = _state

    // Called when the user taps Login.
    fun login(campus: String, username: String, password: String) {
        // Immediately set state to Loading (spinner shows).
        _state.value = UiState.Loading

        // Launch a coroutine tied to the ViewModel lifecycle.
        viewModelScope.launch {
            // Ask Repository to perform login.
            val result = repo.login(campus, username.trim(), password.trim())

            // Convert Result<T> into UiState.
            _state.value = result.fold(
                onSuccess = { UiState.Success(it) },  // success: store keypass string
                onFailure = { UiState.Error(it.message ?: "Login failed") } // error: show message
            )
        }
    }
}

