package com.apro.crypto.sort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.MVIEngine
import com.apro.crypto.mvi.MVIEngineImpl
import com.apro.crypto.sort.models.SortAction
import com.apro.crypto.sort.models.SortOption
import com.apro.crypto.sort.models.SortState
import com.apro.crypto.sort.models.SortType

class SortViewModel(
    private val sortType: SortType,
    private val sortReducer: SortReducer,
    private val sortMiddleware: SortMiddleware,
    private val eventPublisher: EventsSource,
) : ViewModel(), MVIEngine<SortState, SortAction, Nothing> by MVIEngineImpl(
    createInitialState(sortType = sortType),
    reducer = sortReducer,
    middleware = sortMiddleware,
    eventPublisher = eventPublisher
) {

    init {
        viewModelScope.startEngine()
    }

    override fun onCleared() {
        super.onCleared()
        stopEngine()
    }

}

private fun createInitialState(sortType: SortType): SortState =
    SortState(
        options = listOf(
            SortOption(
                title = "Currency name",
                isSelected = sortType == SortType.Name,
                SortType.Name
            ),
            SortOption(
                title = "Current price min -> max",
                sortType == SortType.MinToMax,
                SortType.MinToMax
            ),
            SortOption(
                title = "Current price max -> min",
                sortType == SortType.MaxToMin,
                SortType.MaxToMin
            )
        )
    )