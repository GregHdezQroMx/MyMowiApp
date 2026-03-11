package com.jght.business.mobility.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.domain.TripDestination
import com.jght.business.mobility.domain.TripCostSummary
import com.jght.business.mobility.domain.TripOptimizationInfo
import com.jght.business.mobility.presentation.TripUiState
import com.jght.business.mobility.presentation.TripViewModel

/**
 * PANTALLA DE PROGRAMACIÓN DE VIAJES (Compose Multiplatform)
 * 
 * COMPARACIÓN CON REACT NATIVE (JSX/TSX):
 * 1. RENDIMIENTO: En RN, el renderizado depende de un puente (Bridge) o JSI. 
 *    En CMP, el código se compila a binario nativo (iOS) o Bytecode optimizado (Android).
 * 2. ESTADO: En RN usarías 'useEffect' para cargar datos, lo cual puede causar 
 *    bucles infinitos si no se maneja bien. Aquí, el ViewModel con StateFlow 
 *    mantiene el estado fuera del ciclo de vida de la UI de forma segura.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripBookingScreen(viewModel: TripViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("MOWI - Programar Viaje", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is TripUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is TripUiState.Success -> {
                    TripContent(
                        destinations = state.destinations,
                        selectedDestination = state.selectedDestination,
                        costSummary = state.costSummary,
                        optimization = state.optimization,
                        onSelect = { viewModel.onDestinationSelected(it) }
                    )
                }
                is TripUiState.Error -> Text("Error: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                else -> Unit
            }
        }
    }
}

@Composable
fun TripContent(
    destinations: List<TripDestination>,
    selectedDestination: TripDestination?,
    costSummary: TripCostSummary?,
    optimization: TripOptimizationInfo?,
    onSelect: (TripDestination) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Selecciona tu Destino Corporativo", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        items(destinations) { destination ->
            DestinationCard(
                destination = destination,
                isSelected = destination == selectedDestination,
                onClick = { onSelect(destination) }
            )
        }

        if (optimization != null) {
            item {
                OptimizationCard(optimization)
            }
        }

        if (costSummary != null) {
            item {
                CostSummaryCard(costSummary)
            }
        }

        item {
            Button(
                onClick = { /* Confirmar */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedDestination != null
            ) {
                Text("Confirmar Reservación con IA", color = Color.White)
            }
        }
    }
}

@Composable
fun DestinationCard(destination: TripDestination, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF3F51B5)) else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(destination.name, fontWeight = FontWeight.Bold)
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
                Text(optimization.message, fontSize = 13.sp)
                Text("Ahorro de Tiempo/Costo: ${optimization.savingsPercentage}%", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CostSummaryCard(summary: TripCostSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen de Logística", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tarifa Base")
                Text("${summary.currency} ${summary.subtotal}")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Impuestos Corporativos")
                Text("${summary.currency} ${summary.tax}")
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Estimado", fontWeight = FontWeight.Bold)
                Text("${summary.currency} ${summary.total}", fontWeight = FontWeight.Bold, color = Color(0xFF3F51B5))
            }
        }
    }
}
