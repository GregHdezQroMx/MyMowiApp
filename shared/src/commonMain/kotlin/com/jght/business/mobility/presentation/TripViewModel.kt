package com.jght.business.mobility.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jght.business.mobility.data.TripRepository
import com.jght.business.mobility.domain.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * VIEWMODEL COMPARTIDO (Presentation Logic)
 * 
 * COMPARACIÓN CON REDUX (React Native):
 * En Redux, tendrías un 'reducer', 'actions' y 'thunks' dispersos. 
 * Aquí, el ViewModel encapsula el estado de forma Reactiva y TIPADA.
 * StateFlow es 'thread-safe' por diseño, evitando las 'race conditions' 
 * comunes al actualizar el estado global en JS.
 */
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
                _uiState.value = TripUiState.Loading // O un estado de 'Updating'
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
