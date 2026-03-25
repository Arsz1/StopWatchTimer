package com.example.timer.di

import com.example.timer.domain.FormatTimeUseCase
import com.example.timer.domain.ResetTimerUseCase
import com.example.timer.domain.StartTimerUseCase
import com.example.timer.domain.StopTimerUseCase
import com.example.timer.presentation.StopwatchViewModel
import com.example.timer.presentation.TimerViewModel
import com.example.timer.service.AndroidTimerService
import com.example.timer.service.TimerService
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun initKoin(context: android.content.Context) {
    startKoin {
        androidContext(context)
        modules(
            androidModule,
            domainModule,
            dataModule,
            presentationModule
        )
    }
}

val androidModule = module {
    single { Dispatchers.Main }
    single { Dispatchers.IO }
    single { Dispatchers.Default }
}

val domainModule = module {
    singleOf(::FormatTimeUseCase)
    singleOf(::StartTimerUseCase)
    singleOf(::StopTimerUseCase)
    singleOf(::ResetTimerUseCase)
}

val dataModule = module {
    single<TimerService> { AndroidTimerService() }
}

val presentationModule = module {
    viewModel { TimerViewModel(
        timerService = get(),
        formatTimeUseCase = get()
    ) }
    viewModel { StopwatchViewModel(
        timerService = get(),
        formatTimeUseCase = get()
    ) }
}