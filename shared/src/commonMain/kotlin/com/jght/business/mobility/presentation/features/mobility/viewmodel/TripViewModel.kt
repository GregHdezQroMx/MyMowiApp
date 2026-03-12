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
        val selectedVehicle: VehicleType? = null,
        val userProfile: UserProfile = UserProfile("1", "José Luis", "j.luis@mowi.com")
    ) : TripUiState()
    data class Error(val message: String) : TripUiState()
}

data class LatLng(val lat: Double, val lng: Double)

class TripViewModel(private val repository: TripRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<TripUiState>(TripUiState.Idle)
    val uiState: StateFlow<TripUiState> = _uiState.asStateFlow()

    private val _tripProgress = MutableStateFlow(0f)
    val tripProgress: StateFlow<Float> = _tripProgress.asStateFlow()

    private val _vehicleLocation = MutableStateFlow(LatLng(19.4326, -99.1332))
    val vehicleLocation: StateFlow<LatLng> = _vehicleLocation.asStateFlow()

    private val _isDriverArriving = MutableStateFlow(false)
    val isDriverArriving: StateFlow<Boolean> = _isDriverArriving.asStateFlow()

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
                    vehicleTypes = listOf(
                        VehicleType("corp", "Corporativo", "taxi", 10.0),
                        VehicleType("van", "Vans", "van", 15.0),
                        VehicleType("exec", "Ejecutivo", "car", 25.0)
                    )
                )
            } catch (e: Exception) {
                _uiState.value = TripUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun onDestinationSelected(destination: TripDestination) {
        val currentState = _uiState.value
        if (currentState is TripUiState.Success) {
            viewModelScope.launch {
                try {
                    val costs = repository.calculateCosts(destination.id)
                    val optimization = repository.getAiOptimization(destination.id)
                    _uiState.value = currentState.copy(
                        selectedDestination = destination,
                        costSummary = costs,
                        optimization = optimization
                    )
                } catch (e: Exception) { /* Handle error */ }
            }
        }
    }

    fun onVehicleSelected(vehicle: VehicleType) {
        val currentState = _uiState.value
        if (currentState is TripUiState.Success) {
            _uiState.value = currentState.copy(selectedVehicle = vehicle)
        }
    }

    fun startTrip(durationMinutes: Int) {
        simulationJob?.cancel()
        _tripProgress.value = 0f
        _isDriverArriving.value = true
        simulationJob = viewModelScope.launch {
            delay(3000)
            _isDriverArriving.value = false
            val totalSeconds = durationMinutes
            for (i in 1..totalSeconds) {
                delay(1000)
                val progress = i.toFloat() / totalSeconds
                _tripProgress.value = progress
                // Simulación Manhattan Path
                if (progress < 0.5f) {
                    _vehicleLocation.value = LatLng(19.4326 + (0.01 * progress * 2), -99.1332)
                } else {
                    _vehicleLocation.value = LatLng(19.4426, -99.1332 + (-0.01 * (progress - 0.5) * 2))
                }
            }
        }
    }

    private var simulationJob: Job? = null

    fun resetTrip() {
        simulationJob?.cancel()
        _tripProgress.value = 0f
        _isDriverArriving.value = false
    }
}
