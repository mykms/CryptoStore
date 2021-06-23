package com.apro.crypto.calc

import com.apro.crypto.asMoney
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.Middleware
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CalcMiddleware(
    eventPublisher: EventsSource,
    private val calcInteractor: CalcInteractor
) : Middleware<CalcAction, CalcEffect>(eventPublisher) {

    override fun performAction(action: CalcAction): Flow<CalcEffect> =
        when (action) {
            is CalcAction.Calculate -> action.preform()
            else -> super.performAction(action)
        }

    private fun CalcAction.Calculate.preform() = flow {
        try {
            emit(CalcEffect.CalculationStarted)
            val x = x.toDouble()
            val y = y.toDouble()
            val result = calcInteractor.calculate(x, y)
            emit(CalcEffect.ResultCalculated(result.asMoney()))
        } catch (error: Throwable) {
            sendEvent(CalcEvent.ErrorEvent("error occurred"))
            emit(CalcEffect.ResultCalculated("invalid input"))
        } finally {
            emit(CalcEffect.CalculationFinished)
        }
    }

}