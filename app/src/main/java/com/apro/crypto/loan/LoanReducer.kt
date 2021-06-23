package com.apro.crypto.loan

import com.apro.crypto.mvi.Reducer
import kotlin.math.roundToInt

class LoanReducer(private val interactor: LoanInteractor) :
    Reducer<LoanState, LoanAction, LoanEffect> {

    override fun reduceAction(state: LoanState, action: LoanAction): LoanState =
        when (action) {
            is LoanAction.AmountChanged -> onAmountChanged(state, action)
            is LoanAction.PercentOfChanged -> onPercentOfMaxChanged(state, action)
            is LoanAction.TermsChanged -> onTermsChanged(state, action)
        }

    private fun onPercentOfMaxChanged(
        state: LoanState,
        action: LoanAction.PercentOfChanged
    ): LoanState = state.copy(
        amountOfMoney = (action.value * interactor.getMaxSum()).roundToInt().toString(),
        percentOfMax = action.value
    ).calculated()

    private fun onAmountChanged(state: LoanState, action: LoanAction.AmountChanged): LoanState =
        (action.amountOfMoney.toDoubleOrNull() ?: 0.0).let { amount ->
            state.copy(
                amountOfMoney = action.amountOfMoney,
                percentOfMax = (amount / interactor.getMaxSum()).toFloat()
            ).calculated()
        }

    private fun onTermsChanged(state: LoanState, action: LoanAction.TermsChanged): LoanState =
        state.copy(termOfMonths = action.value).calculated()

    private fun LoanState.calculated(): LoanState {
        val amount = amountOfMoney.toDoubleOrNull() ?: 0.0
        val rate = interactor.getRate(amount, termOfMonths)
        val monthlyPayment = interactor.getMonthlyPayment(amount, termOfMonths)
        val isWithinLimits = interactor.isWithInLimits(amount)
        return copy(
            interestRate = rate.takeIf { isWithinLimits } ?: 10,
            isError = isWithinLimits.not(),
            monthlyPayment = monthlyPayment.takeIf { isWithinLimits } ?: 0.0
        )
    }

}