package com.jght.business.mobility.data.features.mobility.repository

import com.jght.business.mobility.domain.features.mobility.model.*
import kotlinx.coroutines.delay

class TripRepository {
    suspend fun getDestinations(): List<TripDestination> {
        delay(800)
        return listOf(
            TripDestination("1", "Sede Corporativa Mowi", "Av. Libertador 450"),
            TripDestination("2", "Parque Logístico Norte", "Calle 10 Industrial"),
            TripDestination("3", "Aeropuerto Internacional", "Terminal C")
        )
    }

    suspend fun calculateCosts(destId: String): TripCostSummary {
        delay(500)
        return TripCostSummary(25.0, 4.0, 29.0)
    }

    suspend fun getAiOptimization(destId: String): TripOptimizationInfo {
        delay(700)
        return TripOptimizationInfo(
            message = "Ruta optimizada para evitar congestión en el centro.",
            savingsPercentage = 18,
            estimatedTimeMinutes = 15
        )
    }
}
