package com.jght.business.mobility.di

import com.jght.business.mobility.features.mobility.data.repository.TripRepository
import com.jght.business.mobility.features.mobility.presentation.viewmodel.TripViewModel
import org.koin.dsl.module

val appModule = module {
    single { TripRepository() }
    factory { TripViewModel(get()) }
}
