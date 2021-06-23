package com.apro.crypto.calc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.MVIEngine
import com.apro.crypto.mvi.MVIEngineImpl

class CalcViewModel(
    val reducer: CalcReducer,
    val middleware: CalcMiddleware,
    val eventPublisher: EventsSource
) : ViewModel(), MVIEngine<CalcState, CalcAction, CalcEffect> by MVIEngineImpl(
    initialState = CalcState(),
    reducer = reducer,
    middleware = middleware,
    eventPublisher = eventPublisher
) {

    init {
        viewModelScope.startEngine()
    }

    override fun onCleared() {
        stopEngine()
        super.onCleared()
    }

}