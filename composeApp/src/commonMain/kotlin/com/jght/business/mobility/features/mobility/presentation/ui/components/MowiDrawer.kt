package com.jght.business.mobility.features.mobility.presentation.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mymowiapp.composeapp.generated.resources.Res
import mymowiapp.composeapp.generated.resources.about_mowi
import mymowiapp.composeapp.generated.resources.contact_us
import mymowiapp.composeapp.generated.resources.hello_user
import mymowiapp.composeapp.generated.resources.support
import mymowiapp.composeapp.generated.resources.trip_agenda
import mymowiapp.composeapp.generated.resources.trip_history
import mymowiapp.composeapp.generated.resources.view_profile
import org.jetbrains.compose.resources.stringResource

@Composable
fun DrawerContent(
    userName: String,
    onProfileClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAboutClick: () -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(top = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF3F51B5)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        stringResource(Res.string.hello_user, userName),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        stringResource(Res.string.view_profile),
                        color = Color(0xFF7E57C2),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onProfileClick() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFF5F5F5))

        DrawerItem(Icons.AutoMirrored.Filled.List, stringResource(Res.string.trip_history)) {
            onHistoryClick()
        }
        DrawerItem(Icons.Default.DateRange, stringResource(Res.string.trip_agenda)) {
            onClose()
        }
        DrawerItem(Icons.Default.Info, stringResource(Res.string.about_mowi)) {
            onAboutClick()
        }
        DrawerItem(Icons.Default.Build, stringResource(Res.string.support)) {
            onClose()
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(
                    brush = Brush.verticalGradient(listOf(Color(0xFFFAFAFA), Color(0xFFEEEEEE))),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.Black.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    stringResource(Res.string.contact_us),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    "+598 99 017 660",
                    color = Color(0xFF3F51B5),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(24.dp))
        Text(label, fontSize = 16.sp, color = Color.DarkGray)
    }
}
