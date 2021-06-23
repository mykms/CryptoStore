package com.apro.crypto.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apro.crypto.details.models.DetailsAction
import com.apro.crypto.details.models.DetailsEffect
import com.apro.crypto.details.models.DetailsState
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.MVIEngine
import com.apro.crypto.mvi.MVIEngineImpl

class DetailsViewModel(
    args: DetailsArgs,
    reducer: DetailsReducer,
    middleware: DetailsMiddleware,
    eventPublisher: EventsSource
) : ViewModel(),
    MVIEngine<DetailsState, DetailsAction, DetailsEffect> by
    MVIEngineImpl(
        DetailsState(coinId = args.coinId),
        initialAction = DetailsAction.Start(1f),
        reducer = reducer,
        middleware = middleware,
        eventPublisher = eventPublisher
    ) {

    init {
        viewModelScope.startEngine()
    }

    override fun onCleared() {
        super.onCleared()
        stopEngine()
    }
}