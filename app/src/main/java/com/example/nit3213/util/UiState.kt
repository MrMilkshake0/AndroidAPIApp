package com.example.nit3213.util

// UiState is a simple way to describe "what's happening" so the UI can react.
// - Idle: nothing happening yet.
// - Loading: show a progress indicator.
// - Success<T>: operation worked; here’s the data to display.
// - Error: operation failed; show the message.
sealed class UiState<out T> {
    // No operation in progress and no data yet.
    data object Idle : UiState<Nothing>()

    // Work is in progress (e.g., network call). Show spinner.
    data object Loading : UiState<Nothing>()

    // Work finished successfully. Carry the data for the UI to render.
    data class Success<T>(val data: T) : UiState<T>()

    // Work failed. Carry a human-readable error message for the UI to show.
    data class Error(val message: String) : UiState<Nothing>()
}
