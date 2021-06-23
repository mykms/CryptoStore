package com.apro.crypto.details

import com.apro.crypto.details.domain.DetailsInteractor
import com.apro.crypto.details.models.DetailsAction
import com.apro.crypto.details.models.DetailsEffect
import com.apro.crypto.main.domain.MarketDataInteractor
import com.apro.crypto.mvi.EventsSource
import com.apro.crypto.mvi.Middleware
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailsMiddleware(
    private val args: DetailsArgs,
    private val detailsInteractor: DetailsInteractor,
    private val marketDataInteractor: MarketDataInteractor,
    eventPublisher: EventsSource,
) : Middleware<DetailsAction, DetailsEffect>(eventPublisher) {

    override fun performAction(action: DetailsAction): Flow<DetailsEffect> =
        when (action) {
            is DetailsAction.Start -> start(action.amount)
            is DetailsAction.AmountChanged -> changeAmount(
                price = action.coin.price,
                amount = action.value
            )
        }

    private fun start(amount: Float): Flow<DetailsEffect> = flow {
        val coin = marketDataInteractor.getCoinById(args.coinId)
        emit(DetailsEffect.CoinLoaded(coin))
        emit(DetailsEffect.TotalAmount(detailsInteractor.calculate(coin.price, amount.toDouble())))
    }

    private fun changeAmount(price: Double, amount: Float): Flow<DetailsEffect> = flow {
        val total = detailsInteractor.calculate(price, amount.toDouble())
        emit(DetailsEffect.TotalAmount(total))
    }

}
