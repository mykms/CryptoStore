package com.apro.crypto

import android.app.Application
import com.apro.crypto.calc.*
import com.apro.crypto.details.*
import com.apro.crypto.details.domain.DetailsInteractor
import com.apro.crypto.loan.*
import com.apro.crypto.main.*
import com.apro.crypto.main.domain.CartInteractor
import com.apro.crypto.main.domain.MarketDataInteractor
import com.apro.crypto.mvi.EventPublisher
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.order.OrderFragment
import com.apro.crypto.order.OrderMiddleware
import com.apro.crypto.order.OrderReducer
import com.apro.crypto.order.OrderViewModel
import com.apro.crypto.sort.ChooseSortBottomSheet
import com.apro.crypto.sort.SortMiddleware
import com.apro.crypto.sort.SortReducer
import com.apro.crypto.sort.SortViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single { MarketDataInteractor(context = get()) }
    single { CartInteractor() }
    scope<MainFragment> {
        scoped { MainReducer(eventPublisher = get()) }
        scoped {
            MainMiddleware(
                interactor = get(),
                cartInteractor = get(),
                eventPublisher = get()
            )
        }
        scoped<EventsSource> { EventPublisher() }
        viewModel {
            MainViewModel(
                eventPublisher = get(),
                middleware = get(),
                reducer = get()
            )
        }
    }
    scope<DetailsFragment> {
        scoped { DetailsInteractor() }
        scoped<EventsSource> { EventPublisher() }
        scoped { DetailsReducer() }
        scoped {
            DetailsMiddleware(
                args = get(),
                detailsInteractor = get(),
                eventPublisher = get(),
                marketDataInteractor = get()
            )
        }
        viewModel { params ->
            DetailsViewModel(
                args = params.get(),
                reducer = get(),
                middleware = get { params },
                eventPublisher = get()
            )
        }
    }
    scope<OrderFragment> {
        scoped<EventsSource> { EventPublisher() }
        scoped { OrderReducer() }
        scoped { OrderMiddleware(eventPublisher = get(), cartInteractor = get()) }
        viewModel {
            OrderViewModel(
                reducer = get(),
                middleware = get(),
                eventPublisher = get()
            )
        }
    }
    scope<ChooseSortBottomSheet> {
        scoped<EventsSource> { EventPublisher() }
        scoped { SortReducer() }
        scoped { SortMiddleware(eventPublisher = get()) }
        viewModel { params ->
            SortViewModel(
                sortType = params.get(),
                sortReducer = get(),
                sortMiddleware = get(),
                eventPublisher = get(),
            )
        }
    }

    scope<CalcFragment> {
        scoped { CalcInteractor() }
        scoped<EventsSource> { EventPublisher() }
        scoped { CalcReducer() }
        scoped { CalcMiddleware(eventPublisher = get(), calcInteractor = get()) }
        viewModel {
            CalcViewModel(
                reducer = get(),
                middleware = get(),
                eventPublisher = get()
            )
        }
    }

    scope<LoanFragment> {
        scoped { LoanInteractor() }
        scoped { EventPublisher() }
        scoped { LoanReducer(interactor = get()) }
        scoped { LoanMiddleware(eventPublisher = get()) }
        viewModel {
            LoanViewModel(
                reducer = get(),
                middleware = get(),
                eventPublisher = get()
            )
        }
    }

}

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

}