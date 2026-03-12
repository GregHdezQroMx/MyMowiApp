package com.jght.business.mobility.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.domain.features.mobility.model.*
import com.jght.business.mobility.presentation.features.mobility.viewmodel.TripUiState
import com.jght.business.mobility.presentation.features.mobility.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBooking: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Mock de Mapa de fondo
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFEEEEEE))) {
             Text("MAP MOCK", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
            
            Spacer(modifier = Modifier.weight(1f))

            // Card inferior blanca estilo Mowi
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Hola José Luis",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "¿Que te gustaría hacer hoy?",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    ActionItem("Solicitar un viaje", "¿A dónde vamos?", onNavigateToBooking)
                    ActionItem("Envíos", "¿Desea enviar o recibir algo?", {})
                    ActionItem("Agendar un viaje", "Programe su viaje o envío.", {})
                }
            }
        }
    }
}

@Composable
fun ActionItem(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🚖", fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF3F51B5))
            Text(subtitle, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

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
            CenterAlignedTopAppBar(
                title = { Text("MOWI - Solicitar Viaje", fontWeight = FontWeight.Bold) },
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
                is TripUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is TripUiState.Success -> {
                    TripBookingContent(
                        state = state,
                        onDestinationSelect = { viewModel.onDestinationSelected(it) },
                        onVehicleSelect = { viewModel.onVehicleSelected(it) },
                        onConfirm = {
                            val destName = state.selectedDestination?.name ?: "Viaje"
                            val duration = state.optimization?.estimatedTimeMinutes ?: 5
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
    val opt = state.optimization
    val costs = state.costSummary

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Seleccione tipo de viaje", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.vehicleTypes) { vehicle ->
                    VehicleTypeCard(
                        vehicle = vehicle,
                        isSelected = vehicle == state.selectedVehicle,
                        onClick = { onVehicleSelect(vehicle) }
                    )
                }
            }
        }

        item {
            Text("Destino", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        items(state.destinations) { destination ->
            DestinationCard(
                destination = destination,
                isSelected = destination == state.selectedDestination,
                onClick = { onDestinationSelect(destination) }
            )
        }

        if (opt != null) {
            item { OptimizationCard(opt) }
        }

        if (costs != null) {
            item { CostSummaryCard(costs) }
        }

        item {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(12.dp),
                enabled = state.selectedDestination != null && state.selectedVehicle != null
            ) {
                Text("Confirmar", color = Color.White)
            }
        }
    }
}

@Composable
fun VehicleTypeCard(vehicle: VehicleType, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.aspectRatio(1f).clickable { onClick() },
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF3F51B5)) else null,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val icon = when(vehicle.id) {
                "corp" -> "🚕"
                "van" -> "🚐"
                "hour" -> "🕒"
                "exec" -> "🚗"
                "green" -> "🍃"
                else -> "🚙"
            }
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(vehicle.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun ActiveTripScreen(
    destinationName: String,
    progress: Float,
    onTripFinished: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFDDDDDD))) {
            Text("ACTIVE ROUTE MOCK", modifier = Modifier.align(Alignment.Center), color = Color.DarkGray)
        }

        Card(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Viaje a $destinationName", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF3F51B5),
                    trackColor = Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    if (progress < 1f) "Llegando en ${( (1f - progress) * 10).toInt()} min" else "¡Has llegado!",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                if (progress >= 1f) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onTripFinished,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Finalizar")
                    }
                }
            }
        }
    }
}

@Composable
fun DestinationCard(destination: TripDestination, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF3F51B5)) else null,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(destination.name, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(destination.address, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun OptimizationCard(optimization: TripOptimizationInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Optimización por IA MOWI", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Text(optimization.message, fontSize = 13.sp, color = Color.Black)
                Text("Ahorro de Tiempo/Costo: ${optimization.savingsPercentage}%", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun CostSummaryCard(summary: TripCostSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen de Logística", fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tarifa Base", color = Color.Black)
                Text("${summary.currency} ${summary.subtotal}", color = Color.Black)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Impuestos Corporativos", color = Color.Black)
                Text("${summary.currency} ${summary.tax}", color = Color.Black)
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Estimado", fontWeight = FontWeight.Bold, color = Color.Black)
                Text("${summary.currency} ${summary.total}", fontWeight = FontWeight.Bold, color = Color(0xFF3F51B5))
            }
        }
    }
}
