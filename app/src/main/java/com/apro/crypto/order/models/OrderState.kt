package com.apro.crypto.order.models

import com.apro.crypto.R
import com.apro.crypto.main.domain.Coin

data class OrderState(
    val items: List<OrderItem> = listOf(),
    val screen: OrderStateScreen = OrderStateScreen.REVIEW_ITEMS,
    val smsCode: String = "",
    val secondsToResend: Int = 0,
    val isBusy: Boolean = false
)

data class OrderItem(val coin: Coin, val amount: Int, val total: Double)

enum class OrderStateScreen {
    REVIEW_ITEMS,
    SMS_VERIFICATION,
    SUCCESS
}

val OrderState.titleId: Int
    get() = when (screen) {
        OrderStateScreen.REVIEW_ITEMS -> R.string.order_review_items_title
        OrderStateScreen.SMS_VERIFICATION -> R.string.order_sms_title
        OrderStateScreen.SUCCESS -> R.string.order_success
    }

val OrderState.total: Double
    get() = items.sumOf { it.total }

val OrderState.canResendSms: Boolean
    get() = secondsToResend == 0