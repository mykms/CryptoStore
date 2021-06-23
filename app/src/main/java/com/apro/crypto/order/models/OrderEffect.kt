package com.apro.crypto.order.models

sealed interface OrderEffect {
    class OrderItemsLoaded(val items: List<OrderItem>) : OrderEffect
    class PlaceOrder(val secondsToSend: Int) : OrderEffect
    class SecondsChanged(val secondsToSend: Int) : OrderEffect
    object OrderPlaced : OrderEffect
    class BusyStateChange(val isBusy: Boolean) : OrderEffect
    object ResetSMSText : OrderEffect
}