package com.jght.business.mobility.mymowiapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.jght.business.mobility.data.features.mobility.repository.TripRepository
import com.jght.business.mobility.domain.features.mobility.model.TripRoute
import com.jght.business.mobility.presentation.features.mobility.viewmodel.TripViewModel
import com.jght.business.mobility.ui.ActiveTripScreen
import com.jght.business.mobility.ui.HomeScreen
import com.jght.business.mobility.ui.TripBookingScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(TripRoute.Home::class)
            subclass(TripRoute.Selection::class)
            subclass(TripRoute.Active::class)
        }
    }
}

@Composable
fun App() {
    MaterialTheme {
        val repository = remember { TripRepository() }
        val viewModel = remember { TripViewModel(repository) }
        
        val backStack = rememberNavBackStack(config, TripRoute.Home as NavKey)

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (val route = key as TripRoute) {
                    is TripRoute.Home -> NavEntry(key) {
                        HomeScreen(
                            onNavigateToBooking = {
                                backStack.add(TripRoute.Selection("any"))
                            }
                        )
                    }
                    is TripRoute.Selection -> NavEntry(key) {
                        TripBookingScreen(
                            viewModel = viewModel,
                            onNavigateBack = { backStack.removeLastOrNull() },
                            onStartTrip = { name, duration ->
                                // Corregido: startTrip ya no usa lambda, el viaje termina manual
                                viewModel.startTrip(duration)
                                backStack.add(TripRoute.Active(name, duration))
                            }
                        )
                    }
                    is TripRoute.Active -> NavEntry(key) {
                        ActiveTripScreen(
                            viewModel = viewModel,
                            destinationName = route.destinationName,
                            onTripFinished = {
                                viewModel.resetTrip()
                                backStack.clear()
                                backStack.add(TripRoute.Home)
                            }
                        )
                    }
                }
            }
        )
    }
}
