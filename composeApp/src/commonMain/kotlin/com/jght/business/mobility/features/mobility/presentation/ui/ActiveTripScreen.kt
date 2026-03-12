package com.jght.business.mobility.features.mobility.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.features.mobility.presentation.ui.components.CostSummaryCard
import com.jght.business.mobility.features.mobility.presentation.ui.components.MockMapView
import com.jght.business.mobility.features.mobility.presentation.ui.components.OptimizationCard
import com.jght.business.mobility.features.mobility.presentation.viewmodel.TripUiState
import com.jght.business.mobility.features.mobility.presentation.viewmodel.TripViewModel
import mymowiapp.composeapp.generated.resources.Res
import mymowiapp.composeapp.generated.resources.destination_reached
import mymowiapp.composeapp.generated.resources.driver_arrival_desc
import mymowiapp.composeapp.generated.resources.driver_on_the_way
import mymowiapp.composeapp.generated.resources.eta_desc
import mymowiapp.composeapp.generated.resources.finish_and_accept
import mymowiapp.composeapp.generated.resources.trip_summary_title
import mymowiapp.composeapp.generated.resources.trip_to
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTripScreen(
    viewModel: TripViewModel,
    destinationName: String,
    onTripFinished: () -> Unit
) {
    val progress by viewModel.tripProgress.collectAsState()
    val location by viewModel.vehicleLocation.collectAsState()
    val isArriving by viewModel.isDriverArriving.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        MockMapView(
            modifier = Modifier.fillMaxSize(),
            vehicleLocation = location,
            isDriverArriving = isArriving
        )
        Card(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isArriving) stringResource(Res.string.driver_on_the_way) else if (progress >= 1f) stringResource(Res.string.destination_reached) else stringResource(Res.string.trip_to, destinationName),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                if (progress < 1f || isArriving) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                        color = Color(0xFF3F51B5),
                        trackColor = Color(0xFFF5F5F5)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = when {
                        isArriving -> stringResource(Res.string.driver_arrival_desc)
                        progress < 1f -> stringResource(Res.string.eta_desc, ( (1f - progress) * 10).toInt())
                        else -> stringResource(Res.string.trip_summary_title)
                    },
                    fontSize = 14.sp,
                    color = if (progress >= 1f) Color(0xFF2E7D32) else Color.Gray,
                    fontWeight = if (progress >= 1f) FontWeight.Bold else FontWeight.Normal
                )

                AnimatedVisibility(visible = progress >= 1f) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        if (uiState is TripUiState.Success) {
                            val state = uiState as TripUiState.Success
                            state.costSummary?.let { CostSummaryCard(it) }
                            Spacer(modifier = Modifier.height(8.dp))
                            state.optimization?.let { OptimizationCard(it) }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onTripFinished,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(Res.string.finish_and_accept), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
