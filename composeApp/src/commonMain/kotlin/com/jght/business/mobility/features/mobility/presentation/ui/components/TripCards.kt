package com.jght.business.mobility.features.mobility.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.features.mobility.domain.TripCostSummary
import com.jght.business.mobility.features.mobility.domain.TripDestination
import com.jght.business.mobility.features.mobility.domain.TripOptimizationInfo
import com.jght.business.mobility.features.mobility.domain.VehicleType
import mymowiapp.composeapp.generated.resources.Res
import mymowiapp.composeapp.generated.resources.ai_optimization_title
import mymowiapp.composeapp.generated.resources.base_fare
import mymowiapp.composeapp.generated.resources.corporate_insurance
import mymowiapp.composeapp.generated.resources.logistics_detail
import mymowiapp.composeapp.generated.resources.projected_savings
import mymowiapp.composeapp.generated.resources.total_final
import org.jetbrains.compose.resources.stringResource

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
            val icon: ImageVector = when(v.id) {
                "corp" -> Icons.Default.LocalTaxi
                "van" -> Icons.Default.DirectionsBus
                "hour" -> Icons.Default.Schedule
                "exec" -> Icons.Default.DirectionsCar
                "green" -> Icons.Default.Eco
                else -> Icons.Default.DirectionsCar
            }
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = if (isSel) Color(0xFF3F51B5) else Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))
            Text(v.name, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
        }
    }
}

@Composable
fun DestinationCard(d: TripDestination, isSel: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        border = if (isSel) BorderStroke(2.dp, Color(0xFF3F51B5)) else null,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
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
