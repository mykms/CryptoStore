package com.apro.crypto.calc

import android.util.Log
import com.apro.crypto.mvi.Reducer

class CalcReducer : Reducer<CalcState, CalcAction, CalcEffect> {

    override fun reduceAction(state: CalcState, action: CalcAction): CalcState =
        when (action) {
            is CalcAction.ValueXChanged -> state.copy(valueX = action.value)
            is CalcAction.ValueYChanged -> state.copy(valueY = action.value)
            is CalcAction.Calculate -> state
        }.log(state)

    override fun reduceEffect(state: CalcState, effect: CalcEffect): CalcState = when (effect) {
        is CalcEffect.CalculationStarted -> state.copy(isCalculating = true)
        is CalcEffect.CalculationFinished -> state.copy(isCalculating = false)
        is CalcEffect.ResultCalculated -> state.copy(result = effect.result)
    }.log(state)

}

private fun CalcState.log(currentState: CalcState): CalcState =
    also {
        if (this != currentState) {
            Log.d("CALC_STATE", this.toString())
        }
    }