package com.apro.crypto.calc

import com.apro.crypto.mvi.Event

// Actions
sealed interface CalcAction {
    class ValueXChanged(val value: String) : CalcAction
    class ValueYChanged(val value: String) : CalcAction
    class Calculate(val x: String, val y: String) : CalcAction
}

// State
data class CalcState(
    val valueX: String = "",
    val valueY: String = "",
    val result: String = "",
    val isCalculating: Boolean = false
)

// Side effects
sealed interface CalcEffect {
    object CalculationStarted : CalcEffect
    object CalculationFinished : CalcEffect
    class ResultCalculated(val result: String) : CalcEffect
}

// Events
sealed interface CalcEvent : Event {
    class ErrorEvent(val message: String) : CalcEvent
}

