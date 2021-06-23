package com.apro.crypto.details.models

import com.apro.crypto.main.domain.Coin

sealed interface DetailsEffect {
    class CoinLoaded(val coin: Coin) : DetailsEffect
    class TotalAmount(val total: Double) : DetailsEffect
}