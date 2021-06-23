package com.apro.crypto.calc

import kotlinx.coroutines.delay

class CalcInteractor {

    suspend fun calculate(x: Double, y: Double): Double {
        delay(3000)
        return x + y
    }

}