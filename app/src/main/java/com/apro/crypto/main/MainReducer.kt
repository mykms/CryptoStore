package com.apro.crypto.main

import com.apro.crypto.main.models.*
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.Reducer

class MainReducer(private val eventPublisher: EventsSource) :
    Reducer<MainState, MainAction, MainSideEffect>, EventsSource by eventPublisher {

    override fun reduceAction(state: MainState, action: MainAction): MainState =
        when (action) {
            is MainAction.SearchRequest -> state.copy(
                searchText = action.text.uppercase(),
                sortType = action.sortType
            )
            is MainAction.GoBackPressed -> state.copy(closeQuestion = true)
            is MainAction.LeaveDenied -> state.copy(closeQuestion = false)
            else -> super.reduceAction(state, action)
        }

    override fun reduceEffect(state: MainState, effect: MainSideEffect): MainState =
        when (effect) {
            is MainSideEffect.SearchStarted -> state.copy(isLoading = true, failed = false)
            is MainSideEffect.SearchSuccess -> state.copy(
                isLoading = false,
                listState = MainListState.Items(effect.items),
                failed = false
            )
            is MainSideEffect.SearchFailure -> state.copy(
                isLoading = false,
                listState = MainListState.Failure(effect.throwable),
                failed = true
            )
            is MainSideEffect.FavoriteToggle -> state.copy(listState = MainListState.Items(state.items.patch(
                check = { it.model == effect.coin.model },
                patch = {
                    it.copy(
                        isFavorite = effect.coin.isFavorite,
                        isInProcess = false
                    )
                }
            )))
            is MainSideEffect.ToggleStarted -> state.copy(listState = MainListState.Items(state.items.patch(
                check = { it.model == effect.coin.model },
                patch = { it.copy(isInProcess = true) }
            )))
            is MainSideEffect.CartUpdated -> state.copy(cartSum = effect.totalSum)
        }

    private val MainState.items: List<CoinModel>
        get() = (listState as? MainListState.Items)?.value ?: listOf()

}