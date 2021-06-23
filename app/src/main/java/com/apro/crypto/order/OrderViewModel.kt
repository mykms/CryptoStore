package com.apro.crypto.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.MVIEngine
import com.apro.crypto.mvi.MVIEngineImpl
import com.apro.crypto.order.models.OrderAction
import com.apro.crypto.order.models.OrderEffect
import com.apro.crypto.order.models.OrderState

class OrderViewModel(
    reducer: OrderReducer,
    middleware: OrderMiddleware,
    eventPublisher: EventsSource
) : ViewModel(),
    MVIEngine<OrderState, OrderAction, OrderEffect> by MVIEngineImpl(
        OrderState(),
        OrderAction.LoadOrder,
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