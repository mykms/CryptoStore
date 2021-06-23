package com.apro.crypto.mvi

interface Reducer<State, Action, Effect> {
    fun reduceAction(state: State, action: Action): State = state
    fun reduceEffect(state: State, effect: Effect): State = state
}