package com.apro.crypto.sort

import com.apro.crypto.mvi.Reducer
import com.apro.crypto.sort.models.SortAction
import com.apro.crypto.sort.models.SortState

class SortReducer : Reducer<SortState, SortAction, Nothing> {
    override fun reduceAction(state: SortState, action: SortAction): SortState =
        when (action) {
            is SortAction.Select -> state.copy(options = state.options.map {
                it.copy(isSelected = action.option == it)
            })
        }
}