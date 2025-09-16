package com.example.nit3213.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nit3213.data.AssetEntity
import com.example.nit3213.data.Repository
import com.example.nit3213.util.UiState
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<AssetEntity>>>(UiState.Loading)
    val state: StateFlow<UiState<List<AssetEntity>>> = _state

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = repo.getDashboard()
            _state.value = result.fold(
                onSuccess = { resp ->
                    val mapped = resp.entities.map(AssetEntity::fromJson)
                    UiState.Success(mapped)
                },
                onFailure = { UiState.Error(it.message ?: "Failed to load dashboard") }
            )
        }
    }
}