package com.apro.crypto.main.models

sealed interface MainSideEffect {
    object SearchStarted : MainSideEffect
    class SearchSuccess(val items: List<CoinModel>) : MainSideEffect
    class SearchFailure(val throwable: Throwable) : MainSideEffect
    class FavoriteToggle(val coin: CoinModel) : MainSideEffect
    class ToggleStarted(val coin: CoinModel) : MainSideEffect
    class CartUpdated(val totalSum: Double) : MainSideEffect
}