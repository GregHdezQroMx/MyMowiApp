package com.jght.business.mobility.domain

import kotlinx.serialization.Serializable

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

data class TripRequest(
    val destinationId: String,
    val tripType: String,
    val reason: String
)
