package com.jght.business.mobility.presentation.features.mobility.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jght.business.mobility.data.features.mobility.repository.TripRepository
import com.jght.business.mobility.domain.features.mobility.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class TripUiState {
    object Idle : TripUiState()
    object Loading : TripUiState()
    data class Success(
        val destinations: List<TripDestination>,
        val vehicleTypes: List<VehicleType>,
        val costSummary: TripCostSummary? = null,
        val optimization: TripOptimizationInfo? = null,
        val selectedDestination: TripDestination? = null,
        val selectedVehicle: VehicleType? = null
    ) : TripUiState()
    data class Error(val message: String) : TripUiState()
}

class TripViewModel(private val repository: TripRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<TripUiState>(TripUiState.Idle)
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    private val _tripProgress = MutableStateFlow(0f)
    val tripProgress: StateFlow<Float> = _tripProgress.asStateFlow()

    private var simulationJob: Job? = null

    val vehicleTypes = listOf(
        VehicleType("corp", "Corporativo", "taxi", 10.0),
        VehicleType("van", "Vans", "van", 15.0),
        VehicleType("hour", "Viajes por Hora", "clock", 20.0),
        VehicleType("exec", "Ejecutivo", "car", 25.0),
        VehicleType("green", "Green", "leaf", 12.0)
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = TripUiState.Loading
            try {
                val destinations = repository.getDestinations()
                _uiState.value = TripUiState.Success(
                    destinations = destinations,
                    vehicleTypes = vehicleTypes
                )
            } catch (e: Exception) {
                _uiState.value = TripUiState.Error("Error al cargar datos: ${e.message}")
            }
        }
    }

    fun onDestinationSelected(destination: TripDestination) {
        val currentState = _uiState.value
        if (currentState is TripUiState.Success) {
            viewModelScope.launch {
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
                    _uiState.value = TripUiState.Error("Error en la selección: ${e.message}")
                }
            }
        }
    }

    fun onVehicleSelected(vehicle: VehicleType) {
        val currentState = _uiState.value
        if (currentState is TripUiState.Success) {
            _uiState.value = currentState.copy(selectedVehicle = vehicle)
        }
    }

    fun startTrip(durationMinutes: Int, onFinished: () -> Unit) {
        simulationJob?.cancel()
        _tripProgress.value = 0f
        simulationJob = viewModelScope.launch {
            val totalSeconds = durationMinutes // 1 min = 1 sec
            for (i in 1..totalSeconds) {
                delay(1000)
                _tripProgress.value = i.toFloat() / totalSeconds
            }
            onFinished()
        }
    }

    fun resetTrip() {
        simulationJob?.cancel()
        _tripProgress.value = 0f
    }
}
