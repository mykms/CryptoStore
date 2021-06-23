package com.apro.crypto.details.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class DetailsInteractor {

    suspend fun calculate(price: Double, amount: Double): Double = withContext(Dispatchers.IO) {
        delay(2000)
        price * amount.roundToInt()
    }

}