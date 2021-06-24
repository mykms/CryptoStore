package com.apro.crypto.loan

class LoanInteractor {

    fun getRate(sum: Double, termOfMonths: Int): Int =
        when {
            sum > 25000 -> 7
            termOfMonths > 10 -> 8
            else -> 10
        }

    fun isWithInLimits(sum: Double): Boolean =
        sum in MIN_AMOUNT..MAX_AMOUNT

    fun getMaxSum(): Double = MAX_AMOUNT

    fun getMonthlyPayment(amount: Double, termOfMonths: Int): Double =
        amount * (1 + getRate(amount, termOfMonths).toDouble() / 100) / termOfMonths

    companion object {
        private const val MIN_AMOUNT = 1000.0
        private const val MAX_AMOUNT = 30000.0
    }

}