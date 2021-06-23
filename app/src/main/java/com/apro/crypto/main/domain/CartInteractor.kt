package com.apro.crypto.main.domain

import com.apro.crypto.order.models.OrderItem
import kotlinx.coroutines.delay

class CartInteractor {

    private val cart = mutableMapOf<Coin, Int>()

    fun addToCart(coin: Coin) {
        val count = cart.getOrPut(coin, { 0 })
        cart[coin] = count + 1
    }

    val totalSum: Double
        get() = cart.toList()
            .fold(0.0, { res, coinInfo -> res + coinInfo.first.price * coinInfo.second })

    fun getItems(): List<OrderItem> = cart.map {
        OrderItem(
            coin = it.key,
            amount = it.value,
            total = it.key.price * it.value
        )
    }

    fun clearCart() {
        cart.clear()
    }

    suspend fun validateCode(smsCode: String) {
        delay(2000)
        if (VALID_SMS != smsCode) {
            error("Incorrect code!")
        }
    }

    fun removeCoin(coin: Coin) {
        val count = cart[coin] ?: 0
        if (count > 1) {
            cart[coin] = count - 1
        } else {
            cart.remove(coin)
        }
    }

    companion object {
        private const val VALID_SMS = "1234"
    }
}