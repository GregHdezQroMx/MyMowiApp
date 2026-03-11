package com.jght.business.mobility.di

import com.jght.business.mobility.data.TripRepository
import com.jght.business.mobility.presentation.TripViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single { TripRepository() }
    factory { TripViewModel(get()) }
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
