package com.apro.crypto.main

import com.apro.crypto.details.DetailsArgs
import com.apro.crypto.main.domain.CartInteractor
import com.apro.crypto.main.domain.Coin
import com.apro.crypto.main.domain.MarketDataInteractor
import com.apro.crypto.main.models.*
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.Middleware
import com.apro.crypto.mvi.flowWithEvent
import com.apro.crypto.sort.models.SortType
import com.apro.crypto.toArgs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainMiddleware(
    private val interactor: MarketDataInteractor,
    private val cartInteractor: CartInteractor,
    eventPublisher: EventsSource,
) : Middleware<MainAction, MainSideEffect>(eventPublisher) {

    override fun performAction(
        action: MainAction
    ): Flow<MainSideEffect> {
        return when (action) {
            is MainAction.Start -> onStart(action.sortType)
            is MainAction.SearchRequest -> onSearchRequest(action.text, action.sortType)
            is MainAction.ItemClicked -> flow {
                sendEvent(OpenDetails(DetailsArgs(action.coin.id).toArgs()))
            }
            is MainAction.ToggleFavorite -> action.onToggleFavorite()
            is MainAction.Buy -> flow {
                sendEvent(ShowSnackbar(action.coin.name, coin = action.coin))
                cartInteractor.addToCart(action.coin)
                emit(MainSideEffect.CartUpdated(cartInteractor.totalSum))
            }
            is MainAction.UndoBuy -> flow {
                cartInteractor.removeCoin(action.coin)
                emit(MainSideEffect.CartUpdated(cartInteractor.totalSum))
            }
            is MainAction.RefreshCart -> flow {
                emit(MainSideEffect.CartUpdated(cartInteractor.totalSum))
            }
            MainAction.LeaveAgreed -> flowWithEvent(GoBackEvent)
            MainAction.GoBackPressed,
            MainAction.LeaveDenied -> super.performAction(action)
            is MainAction.OpenSortClicked -> flowWithEvent(OpenSortEvent)
        }
    }

    private fun MainAction.ToggleFavorite.onToggleFavorite(): Flow<MainSideEffect> = flow {
        emit(MainSideEffect.ToggleStarted(coin))
        if (coin.isFavorite) {
            interactor.removeFavorite(coin.model)
        } else {
            interactor.addFavorite(coin.model)
        }
        emit(
            MainSideEffect.FavoriteToggle(coin = coin.copy(isFavorite = !coin.isFavorite))
        )
    }

    private fun onSearchRequest(text: String, sortType: SortType): Flow<MainSideEffect> = flow {
        emit(MainSideEffect.SearchStarted)
        try {
            val items =
                if (text.isNotEmpty()) interactor.getCoins(text) else interactor.getFavorites()
            emit(MainSideEffect.SearchSuccess(items.toModels(sortType)))
            if (items.isEmpty()) {
                sendEvent(ShowToast("Not found"))
            }
        } catch (error: Throwable) {
            emit(MainSideEffect.SearchFailure(error))
        }
    }

    private fun onStart(sortType: SortType): Flow<MainSideEffect> = flow {
        emit(MainSideEffect.SearchSuccess(interactor.getFavorites().toModels(sortType)))
    }

    private fun Coin.toModel() =
        CoinModel(model = this, isFavorite = interactor.isCoinInFavorites(this))

    private fun List<Coin>.toModels(sortType: SortType) =
        map { it.toModel() }.sortedWith { lhs, rhs ->
            when (sortType) {
                SortType.Name -> lhs.model.name.compareTo(rhs.model.name)
                SortType.MinToMax -> lhs.model.price.compareTo(rhs.model.price)
                SortType.MaxToMin -> lhs.model.price.compareTo(rhs.model.price) * -1
            }
        }

}


fun <T> List<T>.patch(check: (T) -> Boolean, patch: (T) -> T): List<T> =
    map { if (check(it)) patch(it) else it }

