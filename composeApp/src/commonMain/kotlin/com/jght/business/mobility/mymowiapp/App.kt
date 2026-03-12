package com.jght.business.mobility.mymowiapp

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.jght.business.mobility.features.mobility.data.repository.TripRepository
import com.jght.business.mobility.features.mobility.domain.TripRoute
import com.jght.business.mobility.features.mobility.domain.UserProfile
import com.jght.business.mobility.features.mobility.presentation.ui.AboutScreen
import com.jght.business.mobility.features.mobility.presentation.ui.ActiveTripScreen
import com.jght.business.mobility.features.mobility.presentation.ui.HomeScreen
import com.jght.business.mobility.features.mobility.presentation.ui.ProfileScreen
import com.jght.business.mobility.features.mobility.presentation.ui.TripBookingScreen
import com.jght.business.mobility.features.mobility.presentation.ui.TripHistoryScreen
import com.jght.business.mobility.features.mobility.presentation.ui.components.DrawerContent
import com.jght.business.mobility.features.mobility.presentation.viewmodel.TripUiState
import com.jght.business.mobility.features.mobility.presentation.viewmodel.TripViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(TripRoute.Home::class)
            subclass(TripRoute.Selection::class)
            subclass(TripRoute.Active::class)
            subclass(TripRoute.Profile::class)
            subclass(TripRoute.History::class)
            subclass(TripRoute.About::class)
        }
    }
}

@Composable
fun App() {
    MaterialTheme {
        val repository = remember { TripRepository() }
        val viewModel = remember { TripViewModel(repository) }
        val uiState by viewModel.uiState.collectAsState()
        val backStack = rememberNavBackStack(config, TripRoute.Home as NavKey)
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val currentUser = if (uiState is TripUiState.Success) {
            (uiState as TripUiState.Success).userProfile
        } else {
            UserProfile("0", "User", "")
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    drawerContainerColor = Color.White,
                    drawerShape = RoundedCornerShape(0.dp)
                ) {
                    DrawerContent(
                        userName = currentUser.name,
                        onProfileClick = {
                            scope.launch { drawerState.close() }
                            backStack.add(TripRoute.Profile)
                        },
                        onHistoryClick = {
                            scope.launch { drawerState.close() }
                            backStack.add(TripRoute.History)
                        },
                        onAboutClick = {
                            scope.launch { drawerState.close() }
                            backStack.add(TripRoute.About)
                        },
                        onClose = { scope.launch { drawerState.close() } }
                    )
                }
            }
        ) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->
                    when (val route = key as TripRoute) {
                        is TripRoute.Home -> NavEntry(key) {
                            HomeScreen(
                                onNavigateToBooking = {
                                    backStack.add(TripRoute.Selection("any"))
                                },
                                onOpenDrawer = { scope.launch { drawerState.open() } }
                            )
                        }
                        is TripRoute.Selection -> NavEntry(key) {
                            TripBookingScreen(
                                viewModel = viewModel,
                                onNavigateBack = { backStack.removeLastOrNull() },
                                onStartTrip = { name: String, duration: Int ->
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
                        is TripRoute.Profile -> NavEntry(key) {
                            ProfileScreen(
                                userProfile = currentUser,
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                        is TripRoute.History -> NavEntry(key) {
                            TripHistoryScreen(
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                        is TripRoute.About -> NavEntry(key) {
                            AboutScreen(
                                onNavigateBack = { backStack.removeLastOrNull() }
                            )
                        }
                    }
                }
            )
        }
    }
}
