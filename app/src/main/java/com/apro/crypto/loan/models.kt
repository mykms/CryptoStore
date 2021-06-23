package com.apro.crypto.loan

import com.apro.crypto.mvi.Event

data class LoanState(
    val amountOfMoney: String = "",
    val termOfMonths: Int = 6,
    val percentOfMax: Float = 0f,
    val isError: Boolean = false,
    val interestRate: Int = 10,
    val monthlyPayment: Double = 0.0
)

sealed interface LoanAction {
    class AmountChanged(val amountOfMoney: String) : LoanAction
    class PercentOfChanged(val value: Float) : LoanAction
    class TermsChanged(val value: Int) : LoanAction
}

sealed interface LoanEffect
sealed interface LoanEvent : Event