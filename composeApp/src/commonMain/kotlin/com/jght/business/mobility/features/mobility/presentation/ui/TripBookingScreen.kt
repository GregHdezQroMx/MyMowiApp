package com.jght.business.mobility.features.mobility.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.features.mobility.domain.TripDestination
import com.jght.business.mobility.features.mobility.domain.VehicleType
import com.jght.business.mobility.features.mobility.presentation.ui.components.CostSummaryCard
import com.jght.business.mobility.features.mobility.presentation.ui.components.DestinationCard
import com.jght.business.mobility.features.mobility.presentation.ui.components.OptimizationCard
import com.jght.business.mobility.features.mobility.presentation.ui.components.VehicleTypeCard
import com.jght.business.mobility.features.mobility.presentation.viewmodel.TripUiState
import com.jght.business.mobility.features.mobility.presentation.viewmodel.TripViewModel
import mymowiapp.composeapp.generated.resources.Res
import mymowiapp.composeapp.generated.resources.confirm_trip
import mymowiapp.composeapp.generated.resources.corporate_booking
import mymowiapp.composeapp.generated.resources.frequent_destinations
import mymowiapp.composeapp.generated.resources.select_service
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripBookingScreen(
    viewModel: TripViewModel,
    onNavigateBack: () -> Unit,
    onStartTrip: (String, Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.corporate_booking), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is TripUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is TripUiState.Success -> {
                    TripBookingContent(
                        state = state,
                        onDestinationSelect = { viewModel.onDestinationSelected(it) },
                        onVehicleSelect = { viewModel.onVehicleSelected(it) },
                        onConfirm = {
                            val destName = state.selectedDestination?.name ?: "Corporativo"
                            val duration = state.optimization?.estimatedTimeMinutes ?: 5
                            viewModel.startTrip(duration)
                            onStartTrip(destName, duration)
                        }
                    )
                }
                is TripUiState.Error -> Text("Error: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                else -> Unit
            }
        }
    }
}

@Composable
fun TripBookingContent(
    state: TripUiState.Success,
    onDestinationSelect: (TripDestination) -> Unit,
    onVehicleSelect: (VehicleType) -> Unit,
    onConfirm: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text(stringResource(Res.string.select_service), fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(220.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.vehicleTypes) { v ->
                    VehicleTypeCard(
                        v = v,
                        isSel = v == state.selectedVehicle,
                        onClick = { onVehicleSelect(v) }
                    )
                }
            }
        }
        item { Text(stringResource(Res.string.frequent_destinations), fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        items(state.destinations) { d ->
            DestinationCard(
                d = d,
                isSel = d == state.selectedDestination,
                onClick = { onDestinationSelect(d) }
            )
        }
        state.optimization?.let { item { OptimizationCard(it) } }
        state.costSummary?.let { item { CostSummaryCard(it) } }
        item {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(16.dp),
                enabled = state.selectedDestination != null && state.selectedVehicle != null
            ) {
                Text(stringResource(Res.string.confirm_trip), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
