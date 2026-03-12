package com.jght.business.mobility.features.mobility.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.features.mobility.domain.TripHistoryItem
import mymowiapp.composeapp.generated.resources.Res
import mymowiapp.composeapp.generated.resources.destination_label
import mymowiapp.composeapp.generated.resources.origin_label
import mymowiapp.composeapp.generated.resources.see_more
import mymowiapp.composeapp.generated.resources.trip_history
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripHistoryScreen(
    onNavigateBack: () -> Unit
) {
    val historyItems = listOf(
        TripHistoryItem("1", "Cancelado", "30/08/2024-17:08", "Avenida Intercomunal La Trinidad El...", "CCCT, Calle Ernesto Blohm, Caraca..."),
        TripHistoryItem("2", "Cancelado", "30/08/2024-17:08", "Avenida Intercomunal La Trinidad El...", "CCCT, Calle Ernesto Blohm, Caraca..."),
        TripHistoryItem("3", "Cobrado", "30/08/2024-17:08", "FF28+J5 Guatire, Miranda, Venezuela", "CCCT, Calle Ernesto Blohm, Caraca..."),
        TripHistoryItem("4", "Cancelado", "30/08/2024-17:08", "FF27+8P4, Guatire 1221, Miranda, V...", "CCCT, Calle Ernesto Blohm, Caraca...")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(stringResource(Res.string.trip_history), fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(historyItems) { item ->
                HistoryCard(item)
            }
        }
    }
}

@Composable
fun HistoryCard(item: TripHistoryItem) {
    val isCancelled = item.status.lowercase().contains("cancel")
    val statusColor = if (isCancelled) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
    val statusTextColor = if (isCancelled) Color(0xFFD32F2F) else Color(0xFF2E7D32)
    val statusIcon = if (isCancelled) Icons.Default.Close else Icons.Default.CheckCircle

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = statusColor,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(statusIcon, contentDescription = null, tint = statusTextColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(item.status, color = statusTextColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Text(item.date, fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row {
                    Text(stringResource(Res.string.origin_label), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.origin, fontSize = 13.sp, color = Color.DarkGray, maxLines = 1)
                }
                Row {
                    Text(stringResource(Res.string.destination_label), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.destination, fontSize = 13.sp, color = Color.DarkGray, maxLines = 1)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.see_more),
                color = Color(0xFF3F51B5),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
