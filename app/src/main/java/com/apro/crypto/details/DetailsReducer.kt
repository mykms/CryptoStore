package com.apro.crypto.details

import com.apro.crypto.details.models.DetailsAction
import com.apro.crypto.details.models.DetailsEffect
import com.apro.crypto.details.models.DetailsState
import com.apro.crypto.mvi.Reducer

class DetailsReducer : Reducer<DetailsState, DetailsAction, DetailsEffect> {

    override fun reduceAction(state: DetailsState, action: DetailsAction): DetailsState =
        when (action) {
            is DetailsAction.AmountChanged -> state.copy(amount = action.value, calculating = true)
            else -> super.reduceAction(state, action)
        }

    override fun reduceEffect(state: DetailsState, effect: DetailsEffect): DetailsState =
        when (effect) {
            is DetailsEffect.CoinLoaded -> state.copy(
                coin = effect.coin,
                total = effect.coin.price,
                calculating = true
            )
            is DetailsEffect.TotalAmount -> state.copy(total = effect.total, calculating = false)
        }

}