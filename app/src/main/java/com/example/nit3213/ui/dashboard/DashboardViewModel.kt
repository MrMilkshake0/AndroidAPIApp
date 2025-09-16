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

// ViewModel = "brain" for the DashboardActivity.
// It requests dashboard data from the Repository and exposes it as UiState<List<AssetEntity>>.
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {

    // StateFlow holds the current dashboard state.
    // Initially set to Loading so the spinner shows right away.
    private val _state = MutableStateFlow<UiState<List<AssetEntity>>>(UiState.Loading)
    val state: StateFlow<UiState<List<AssetEntity>>> = _state

    // Called by DashboardActivity when the screen is created.
    fun load() {
        // Set state to Loading again in case of reload.
        _state.value = UiState.Loading

        // Launch a coroutine tied to the ViewModel lifecycle.
        viewModelScope.launch {
            val result = repo.getDashboard() // ask Repository to fetch data

            // Convert Result<DashboardResponse> into UiState<List<AssetEntity>>
            _state.value = result.fold(
                onSuccess = { resp ->
                    // Map raw JsonObjects → AssetEntity objects for easy UI use
                    val mapped = resp.entities.map(AssetEntity::fromJson)
                    UiState.Success(mapped)
                },
                onFailure = {
                    UiState.Error(it.message ?: "Failed to load dashboard")
                }
            )
        }
    }
}
