package com.apro.crypto.order.models

import com.apro.crypto.main.domain.Coin
import com.apro.crypto.mvi.Event

sealed interface OrderAction {
    object LoadOrder : OrderAction
    class Buy(val isFinished: Boolean = false) : OrderAction
    class RemoveItem(val coin: Coin) : OrderAction
    class SmsCodeEntered(val text: String) : OrderAction
    class GoBackPressed(val currentScreen: OrderStateScreen) : OrderAction
}

interface OrderEvent : Event {
    object FinishPlacing : OrderEvent
}
