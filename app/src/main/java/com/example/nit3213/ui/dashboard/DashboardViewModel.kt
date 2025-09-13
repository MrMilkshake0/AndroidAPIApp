package com.example.nit3213.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nit3213.data.DashboardResponse
import com.example.nit3213.data.Repository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val items: List<JsonObject> = emptyList()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state

    fun load() {
        _state.value = DashboardUiState(loading = true)
        viewModelScope.launch {
            val result = repo.getDashboard()
            _state.value = result.fold(
                onSuccess = { res: DashboardResponse -> DashboardUiState(items = res.entities) },
                onFailure = { DashboardUiState(error = it.message ?: "Load failed") }
            )
        }
    }
}
