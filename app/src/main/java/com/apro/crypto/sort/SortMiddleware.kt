package com.apro.crypto.sort

import com.apro.crypto.main.models.OpenSortResult
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.Middleware
import com.apro.crypto.mvi.flowWithEvent
import com.apro.crypto.sort.models.SortAction
import kotlinx.coroutines.flow.Flow

class SortMiddleware(eventPublisher: EventsSource) :
    Middleware<SortAction, Nothing>(eventPublisher) {

    override fun performAction(action: SortAction): Flow<Nothing> =
        when (action) {
            is SortAction.Select -> flowWithEvent(OpenSortResult(action.option.sortType))
        }

}