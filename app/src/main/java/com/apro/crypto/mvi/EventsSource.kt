package com.apro.crypto.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

interface EventsSender {
    suspend fun sendEvent(event: Event)
}

interface EventsSource : EventsSender {
    val events: Flow<Event>
}

class EventPublisher : EventsSource, EventsSender {

    private val eventsFlow: MutableSharedFlow<Event> = MutableSharedFlow()

    override suspend fun sendEvent(event: Event) {
        eventsFlow.emit(event)
    }

    override val events: Flow<Event> = eventsFlow
}

fun <T> EventsSender.flowWithEvent(event: Event): Flow<T> = flow { sendEvent(event) }