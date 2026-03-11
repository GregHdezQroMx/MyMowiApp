package com.jght.business.mobility.data

import com.jght.business.mobility.domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * REPOSITORIO DE VIAJES (MOCK)
 * 
 * COMPARACIÓN CON REACT NATIVE:
 * En RN, solemos usar un Service con Axios o Fetch. Aquí usamos Ktor.
 * La ventaja es el TIPADO FUERTE. Mientras que en Redux/JS los datos pueden
 * cambiar de forma impredecible, aquí el compilador garantiza que 'TripCostSummary'
 * siempre tenga la estructura correcta.
 */
class TripRepository {

    // Simulación de API con Ktor (Mockeado para este ejemplo)
    suspend fun getDestinations(): List<TripDestination> {
        delay(1000) // Simular latencia de red
        return listOf(
            TripDestination("1", "Oficina Central", "Av. Principal 123"),
            TripDestination("2", "Centro de Distribución", "Zona Industrial B"),
            TripDestination("3", "Aeropuerto", "Terminal 1, Salidas")
        )
    }

    suspend fun calculateCosts(destinationId: String): TripCostSummary {
        delay(800)
        return TripCostSummary(
            subtotal = 15.50,
            tax = 2.48,
            total = 17.98
        )
    }

    suspend fun getAiOptimization(destinationId: String): TripOptimizationInfo {
        delay(1200)
        return TripOptimizationInfo(
            message = "Ruta optimizada: Ahorro de 15% evitando tráfico en Av. Principal.",
            savingsPercentage = 15,
            estimatedTimeMinutes = 22
        )
    }
}
