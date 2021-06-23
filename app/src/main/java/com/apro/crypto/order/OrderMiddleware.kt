package com.apro.crypto.order

import com.apro.crypto.main.domain.CartInteractor
import com.apro.crypto.main.models.GoBackEvent
import com.apro.crypto.main.models.KeyboardClose
import com.apro.crypto.main.models.RequestFocus
import com.apro.crypto.main.models.ShowToast
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.Middleware
import com.apro.crypto.order.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderMiddleware(
    eventPublisher: EventsSource,
    private val cartInteractor: CartInteractor
) : Middleware<OrderAction, OrderEffect>(eventPublisher) {

    override fun performAction(action: OrderAction): Flow<OrderEffect> =
        when (action) {
            OrderAction.LoadOrder -> flow {
                emit(
                    OrderEffect.OrderItemsLoaded(
                        items = cartInteractor.getItems()
                    )
                )
            }
            is OrderAction.Buy -> flow {
                if (action.isFinished) return@flow
                emit(OrderEffect.PlaceOrder(SECONDS))
                repeat(SECONDS) {
                    delay(1000)
                    emit(OrderEffect.SecondsChanged(SECONDS - it.inc()))
                }
            }
            is OrderAction.SmsCodeEntered -> flow {
                if (action.text.length >= 4) {
                    try {
                        emit(OrderEffect.BusyStateChange(true))
                        cartInteractor.validateCode(action.text)
                        emit(OrderEffect.BusyStateChange(false))
                        cartInteractor.clearCart()
                        sendEvent(OrderEvent.FinishPlacing)
                        sendEvent(KeyboardClose)
                        emit(OrderEffect.OrderPlaced)
                    } catch (error: Throwable) {
                        emit(OrderEffect.BusyStateChange(false))
                        emit(OrderEffect.ResetSMSText)
                        sendEvent(ShowToast(error.message ?: ""))
                        sendEvent(RequestFocus)
                    }
                }
            }
            is OrderAction.GoBackPressed -> flow {
                if (action.currentScreen != OrderStateScreen.SMS_VERIFICATION)
                    sendEvent(GoBackEvent)
            }
            is OrderAction.RemoveItem -> flow {
                cartInteractor.removeCoin(action.coin)
                val items = cartInteractor.getItems()
                if (items.isEmpty()) {
                    sendEvent(GoBackEvent)
                } else {
                    emit(OrderEffect.OrderItemsLoaded(items))
                }
            }
        }

    companion object {
        private const val SECONDS = 15
    }
}