package com.apro.crypto.main.models

import com.apro.crypto.main.domain.Coin
import com.apro.crypto.mvi.NonCancelableAction
import com.apro.crypto.sort.models.SortType

sealed interface MainAction {
    class Start(val sortType: SortType) : MainAction
    class SearchRequest(val text: String, val sortType: SortType) : MainAction

    class ItemClicked(val coin: Coin) : MainAction
    class ToggleFavorite(val coin: CoinModel) : MainAction, NonCancelableAction

    class Buy(val coin: Coin) : MainAction
    class UndoBuy(val coin: Coin) : MainAction

    // Exit with question
    object GoBackPressed : MainAction
    object LeaveAgreed : MainAction
    object LeaveDenied : MainAction

    object RefreshCart : MainAction

    object OpenSortClicked : MainAction
}

