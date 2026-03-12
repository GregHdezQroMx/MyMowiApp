package com.jght.business.mobility.features.mobility.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.business.mobility.features.mobility.presentation.ui.components.MockMapView
import com.jght.business.mobility.features.mobility.presentation.viewmodel.LatLng
import mymowiapp.composeapp.generated.resources.Res
import mymowiapp.composeapp.generated.resources.hello_user
import mymowiapp.composeapp.generated.resources.mowi_shipments
import mymowiapp.composeapp.generated.resources.request_trip
import mymowiapp.composeapp.generated.resources.schedule_desc
import mymowiapp.composeapp.generated.resources.schedule_trip
import mymowiapp.composeapp.generated.resources.shipments_desc
import mymowiapp.composeapp.generated.resources.what_to_do
import mymowiapp.composeapp.generated.resources.where_to_go
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBooking: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MockMapView(
            modifier = Modifier.fillMaxSize(),
            vehicleLocation = LatLng(19.4326, -99.1332)
        )
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onOpenDrawer,
                        modifier = Modifier.padding(8.dp).background(Color.White.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
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
                        title = stringResource(Res.string.request_trip),
                        subtitle = stringResource(Res.string.where_to_go),
                        onClick = onNavigateToBooking
                    )
                    ActionItem(
                        title = stringResource(Res.string.mowi_shipments),
                        subtitle = stringResource(Res.string.shipments_desc),
                        onClick = {}
                    )
                    ActionItem(
                        title = stringResource(Res.string.schedule_trip),
                        subtitle = stringResource(Res.string.schedule_desc),
                        onClick = {}
                    )
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
                .size(52.dp)
                .background(Color(0xFFF8F8F8), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🚖", fontSize = 26.sp)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E),
                fontSize = 16.sp
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}
