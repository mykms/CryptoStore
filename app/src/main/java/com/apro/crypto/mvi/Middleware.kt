package com.apro.crypto.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

abstract class Middleware<Action, Effect>(private val eventsSender: EventsSender) :
    EventsSender by eventsSender {

    open fun performAction(action: Action): Flow<Effect> =
        emptyFlow()
}