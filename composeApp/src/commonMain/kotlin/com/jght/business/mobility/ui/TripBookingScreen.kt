package com.jght.business.mobility.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.domain.features.mobility.model.TripCostSummary
import com.jght.business.mobility.domain.features.mobility.model.TripDestination
import com.jght.business.mobility.domain.features.mobility.model.TripOptimizationInfo
import com.jght.business.mobility.domain.features.mobility.model.VehicleType
import com.jght.business.mobility.presentation.features.mobility.viewmodel.LatLng
import com.jght.business.mobility.presentation.features.mobility.viewmodel.TripUiState
import com.jght.business.mobility.presentation.features.mobility.viewmodel.TripViewModel
import mymowiapp.composeapp.generated.resources.Res
import mymowiapp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun MockMapView(
    modifier: Modifier = Modifier,
    vehicleLocation: LatLng,
    isDriverArriving: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = modifier.background(Color(0xFFF0F0F0))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val stepX = w / 10
            val stepY = h / 10
            
            for (i in 0..10) {
                drawLine(Color.White, Offset(0f, i * stepY), Offset(w, i * stepY), 3.dp.toPx())
                drawLine(Color.White, Offset(i * stepX, 0f), Offset(i * stepX, h), 3.dp.toPx())
            }

            val startPos = Offset(stepX * 2, stepY * 8)
            val cornerPos = Offset(stepX * 2, stepY * 2)
            val endPos = Offset(stepX * 8, stepY * 2)
            
            val routePath = Path().apply {
                moveTo(startPos.x, startPos.y)
                lineTo(cornerPos.x, cornerPos.y)
                lineTo(endPos.x, endPos.y)
            }
            
            drawPath(
                path = routePath,
                color = Color(0xFFBDBDBD),
                style = Stroke(width = 6.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f))
            )

            drawCircle(Color(0xFF3F51B5), 8.dp.toPx(), startPos)
            drawCircle(Color(0xFF2E7D32), 8.dp.toPx(), endPos)

            val latRange = 0.01
            val lngRange = -0.01
            val currentY = startPos.y + (cornerPos.y - startPos.y) * ((vehicleLocation.lat - 19.4326) / latRange).toFloat()
            val currentX = cornerPos.x + (endPos.x - cornerPos.x) * ((vehicleLocation.lng - (-99.1332)) / lngRange).toFloat()
            val currentOffset = Offset(currentX, currentY)

            if (isDriverArriving) {
                drawCircle(Color(0xFF3F51B5).copy(alpha = pulseAlpha), 25.dp.toPx(), currentOffset)
            }
            
            drawCircle(Color.White, 10.dp.toPx(), currentOffset)
            drawCircle(Color.Black, 7.dp.toPx(), currentOffset)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBooking: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MockMapView(modifier = Modifier.fillMaxSize(), vehicleLocation = LatLng(19.4326, -99.1332))
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier.padding(8.dp).background(Color.White.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(Res.string.hello_user, "José Luis"),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = stringResource(Res.string.what_to_do),
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    ActionItem(
                        stringResource(Res.string.request_trip),
                        stringResource(Res.string.where_to_go),
                        onNavigateToBooking
                    )
                    ActionItem(
                        stringResource(Res.string.mowi_shipments),
                        stringResource(Res.string.shipments_desc),
                        {}
                    )
                    ActionItem(
                        stringResource(Res.string.schedule_trip),
                        stringResource(Res.string.schedule_desc),
                        {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripBookingScreen(viewModel: TripViewModel, onNavigateBack: () -> Unit, onStartTrip: (String, Int) -> Unit) {
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
                is TripUiState.Success -> TripBookingContent(
                    state = state,
                    onDestinationSelect = { viewModel.onDestinationSelected(it) },
                    onVehicleSelect = { viewModel.onVehicleSelected(it) }
                ) {
                    val destName = state.selectedDestination?.name ?: "Corporativo"
                    val duration = state.optimization?.estimatedTimeMinutes ?: 5
                    viewModel.startTrip(duration)
                    onStartTrip(destName, duration)
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
                    VehicleTypeCard(v, v == state.selectedVehicle) { onVehicleSelect(v) }
                }
            }
        }
        item { Text(stringResource(Res.string.frequent_destinations), fontSize = 18.sp, fontWeight = FontWeight.Bold) }
        items(state.destinations) { d ->
            DestinationCard(d, d == state.selectedDestination) { onDestinationSelect(d) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTripScreen(viewModel: TripViewModel, destinationName: String, onTripFinished: () -> Unit) {
    val progress by viewModel.tripProgress.collectAsState()
    val location by viewModel.vehicleLocation.collectAsState()
    val isArriving by viewModel.isDriverArriving.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        MockMapView(modifier = Modifier.fillMaxSize(), vehicleLocation = location, isDriverArriving = isArriving)
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

@Composable
fun ActionItem(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(52.dp).background(Color(0xFFF8F8F8), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
            Text("🚖", fontSize = 26.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E), fontSize = 16.sp)
            Text(subtitle, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun VehicleTypeCard(v: VehicleType, isSel: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.aspectRatio(1f).clickable { onClick() },
        border = if (isSel) BorderStroke(2.dp, Color(0xFF3F51B5)) else null,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSel) Color(0xFFF5F5F5) else Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val icon = when(v.id) {
                "corp" -> "🚕"; "van" -> "🚐"; "hour" -> "🕒"; "exec" -> "🚗"; "green" -> "🍃"; else -> "🚙"
            }
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(v.name, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
        }
    }
}

@Composable
fun DestinationCard(d: TripDestination, isSel: Boolean, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }, border = if (isSel) BorderStroke(2.dp, Color(0xFF3F51B5)) else null, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(d.name, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 16.sp)
            Text(d.address, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun OptimizationCard(opt: TripOptimizationInfo) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(Res.string.ai_optimization_title), fontWeight = FontWeight.ExtraBold, color = Color(0xFF2E7D32), fontSize = 14.sp)
            Text(opt.message, fontSize = 13.sp, color = Color.DarkGray)
            Text(stringResource(Res.string.projected_savings, opt.savingsPercentage), fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CostSummaryCard(summary: TripCostSummary) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(Res.string.logistics_detail), fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(Res.string.base_fare), color = Color.Gray, fontSize = 14.sp)
                Text("${summary.currency} ${summary.subtotal}", color = Color.Black, fontWeight = FontWeight.Medium)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(Res.string.corporate_insurance), color = Color.Gray, fontSize = 14.sp)
                Text("${summary.currency} ${summary.tax}", color = Color.Black, fontWeight = FontWeight.Medium)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(Res.string.total_final), fontWeight = FontWeight.ExtraBold, color = Color.Black, fontSize = 18.sp)
                Text("${summary.currency} ${summary.total}", fontWeight = FontWeight.ExtraBold, color = Color(0xFF3F51B5), fontSize = 18.sp)
            }
        }
    }
}
