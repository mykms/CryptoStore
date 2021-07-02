package com.apro.crypto.details.models

import com.apro.crypto.asMoney
import com.apro.crypto.main.domain.Coin

data class DetailsState(
    val coinId: String,
    val coin: Coin? = null,
    val amount: Float = 1f,
    val total: Double = 0.0,
    val closeQuestion: Boolean = false,
    val calculating: Boolean = false
)

val DetailsState.calcText: String
    get() = if (calculating) calcTextConstant else total.asMoney()

private const val calcTextConstant = "calculating..."