package com.jght.business.mobility.mymowiapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(TripRoute.Home::class, TripRoute.Home.serializer())
            subclass(TripRoute.Selection::class, TripRoute.Selection.serializer())
            subclass(TripRoute.Active::class, TripRoute.Active.serializer())
        }
    }
}

@Composable
fun App() {
    MaterialTheme {
        val repository = remember { TripRepository() }
        val viewModel = remember { TripViewModel(repository) }
        
        // Configuración idéntica a KommonHotel
        val backStack = rememberNavBackStack(config, TripRoute.Home as NavKey)
        val tripProgress by viewModel.tripProgress.collectAsState()

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
                                viewModel.startTrip(duration) {
                                    backStack.clear()
                                    backStack.add(TripRoute.Home)
                                }
                                backStack.add(TripRoute.Active(name, duration))
                            }
                        )
                    }
                    is TripRoute.Active -> NavEntry(key) {
                        ActiveTripScreen(
                            destinationName = route.destinationName,
                            progress = tripProgress,
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
