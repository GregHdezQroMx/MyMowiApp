package com.jght.business.mobility.presentation.features.mobility.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jght.business.mobility.data.features.mobility.repository.TripRepository
import com.jght.business.mobility.domain.features.mobility.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class TripUiState {
    object Idle : TripUiState()
    object Loading : TripUiState()
    data class Success(
        val destinations: List<TripDestination>,
        val costSummary: TripCostSummary? = null,
        val optimization: TripOptimizationInfo? = null,
        val selectedDestination: TripDestination? = null
    ) : TripUiState()
    data class Error(val message: String) : TripUiState()
}

class TripViewModel(private val repository: TripRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<TripUiState>(TripUiState.Idle)
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = TripUiState.Loading
            try {
                val destinations = repository.getDestinations()
                _uiState.value = TripUiState.Success(destinations = destinations)
            } catch (e: Exception) {
                _uiState.value = TripUiState.Error("Error al cargar destinos: ${e.message}")
            }
        }
    }

    fun onDestinationSelected(destination: TripDestination) {
        val currentState = _uiState.value
        if (currentState is TripUiState.Success) {
            viewModelScope.launch {
                // Mantenemos los destinos actuales pero mostramos estado de carga para los detalles
                _uiState.value = TripUiState.Loading 
                try {
                    val costs = repository.calculateCosts(destination.id)
                    val optimization = repository.getAiOptimization(destination.id)
                    
                    _uiState.value = currentState.copy(
                        selectedDestination = destination,
                        costSummary = costs,
                        optimization = optimization
                    )
                } catch (e: Exception) {
                    _uiState.value = TripUiState.Error("Error en la optimización: ${e.message}")
                }
            }
        }
    }
}
