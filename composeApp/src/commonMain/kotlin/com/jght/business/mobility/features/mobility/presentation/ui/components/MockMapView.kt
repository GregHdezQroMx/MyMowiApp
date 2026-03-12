package com.jght.business.mobility.features.mobility.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jght.business.mobility.features.mobility.presentation.viewmodel.LatLng

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
