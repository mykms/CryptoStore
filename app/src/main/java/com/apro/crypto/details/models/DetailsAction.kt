package com.apro.crypto.details.models

import com.apro.crypto.main.domain.Coin

sealed interface DetailsAction {
    class Start(val amount: Float) : DetailsAction
    class AmountChanged(val coin: Coin, val value: Float) : DetailsAction
}