package com.apro.crypto.loan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apro.crypto.mvi.EventPublisher
import com.apro.crypto.mvi.MVIEngine
import com.apro.crypto.mvi.MVIEngineImpl

class LoanViewModel(
    private val reducer: LoanReducer,
    private val middleware: LoanMiddleware,
    private val eventPublisher: EventPublisher
) : ViewModel(), MVIEngine<LoanState, LoanAction, LoanEffect> by MVIEngineImpl(
    reducer = reducer,
    initialState = LoanState(),
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