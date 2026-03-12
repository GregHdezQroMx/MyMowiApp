package com.jght.business.mobility.features.mobility.domain

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val role: String = "Corporate"
)

@Serializable
data class TripDestination(
    val id: String,
    val name: String,
    val address: String
)

@Serializable
data class TripCostSummary(
    val subtotal: Double,
    val tax: Double,
    val total: Double,
    val currency: String = "USD"
)

@Serializable
data class TripOptimizationInfo(
    val message: String,
    val savingsPercentage: Int,
    val estimatedTimeMinutes: Int
)

@Serializable
data class VehicleType(
    val id: String,
    val name: String,
    val iconName: String,
    val basePrice: Double
)

@Serializable
sealed class TripRoute : NavKey {
    @Serializable data object Home : TripRoute()
    @Serializable data class Selection(val destinationId: String) : TripRoute()
    @Serializable data class Active(val destinationName: String, val durationMinutes: Int) : TripRoute()
}
