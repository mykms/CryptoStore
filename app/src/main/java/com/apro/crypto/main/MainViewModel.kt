package com.apro.crypto.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apro.crypto.main.models.MainAction
import com.apro.crypto.main.models.MainSideEffect
import com.apro.crypto.main.models.MainState
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.MVIEngine
import com.apro.crypto.mvi.MVIEngineImpl
import com.apro.crypto.sort.models.SortType

class MainViewModel(
    private val eventPublisher: EventsSource,
    private val middleware: MainMiddleware,
    private val reducer: MainReducer,
) : ViewModel(), MVIEngine<MainState, MainAction, MainSideEffect> by MVIEngineImpl(
    initialState = MainState(),
    initialAction = MainAction.Start(SortType.Name),
    eventPublisher = eventPublisher,
    reducer = reducer,
    middleware = middleware
) {

    init {
        viewModelScope.startEngine()
    }

    override fun onCleared() {
        super.onCleared()
        stopEngine()
    }

}