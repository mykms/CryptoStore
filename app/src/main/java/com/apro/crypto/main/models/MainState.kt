package com.apro.crypto.main.models

import com.apro.crypto.main.domain.Coin
import com.apro.crypto.sort.models.SortType

data class MainState(
    val isLoading: Boolean = false,
    val searchText: String = "",
    val listState: MainListState = MainListState.NotSet,
    var failed: Boolean = false,
    val cartSum: Double = 0.0,
    val closeQuestion: Boolean = false,
    val sortType: SortType = SortType.Name
)

sealed interface MainListState {
    object NotSet : MainListState
    class Items(val value: List<CoinModel>) : MainListState
    class Failure(val throwable: Throwable) : MainListState
}

data class CoinModel(
    val model: Coin,
    val isFavorite: Boolean,
    val isInProcess: Boolean = false,
)