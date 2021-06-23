package com.apro.crypto.order

import com.apro.crypto.mvi.Reducer
import com.apro.crypto.order.models.OrderAction
import com.apro.crypto.order.models.OrderEffect
import com.apro.crypto.order.models.OrderState
import com.apro.crypto.order.models.OrderStateScreen

class OrderReducer : Reducer<OrderState, OrderAction, OrderEffect> {
    override fun reduceAction(state: OrderState, action: OrderAction): OrderState =
        when (action) {
            is OrderAction.SmsCodeEntered -> state.copy(smsCode = action.text)
            is OrderAction.GoBackPressed -> {
                if (action.currentScreen == OrderStateScreen.SMS_VERIFICATION) {
                    state.copy(
                        screen = OrderStateScreen.REVIEW_ITEMS,
                        secondsToResend = 0,
                        smsCode = ""
                    )
                } else {
                    state
                }
            }
            else -> super.reduceAction(state, action)
        }

    override fun reduceEffect(state: OrderState, effect: OrderEffect): OrderState =
        when (effect) {
            is OrderEffect.PlaceOrder -> state.copy(
                screen = OrderStateScreen.SMS_VERIFICATION,
                secondsToResend = effect.secondsToSend
            )
            is OrderEffect.OrderItemsLoaded -> state.copy(items = effect.items)
            is OrderEffect.SecondsChanged -> state.copy(secondsToResend = effect.secondsToSend)
            is OrderEffect.OrderPlaced -> state.copy(screen = OrderStateScreen.SUCCESS)
            is OrderEffect.BusyStateChange -> state.copy(isBusy = effect.isBusy)
            is OrderEffect.ResetSMSText -> state.copy(smsCode = "")
        }
}