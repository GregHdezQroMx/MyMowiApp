package com.jght.business.mobility.mymowiapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.jght.business.mobility.ui.TripBookingScreen
import com.jght.business.mobility.presentation.features.mobility.viewmodel.TripViewModel
import com.jght.business.mobility.data.features.mobility.repository.TripRepository
import com.jght.business.mobility.ui.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val repository = TripRepository()
        val viewModel = TripViewModel(repository)
        
        TripBookingScreen(viewModel = viewModel)
    }
}
