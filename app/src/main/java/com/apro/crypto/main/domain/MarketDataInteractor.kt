package com.apro.crypto.main.domain

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.random.Random

class MarketDataInteractor constructor(context: Context) {

    private val randomDelay = Random(Date().time)
    private val _favorites = mutableSetOf<Coin>()

    private val allCoins: List<Coin> =
        context.assets.open("data.json").bufferedReader().use { it.readText() }.let {
            Json {
                ignoreUnknownKeys = true
            }.decodeFromString(it)
        }

    init {
        _favorites.addAll(allCoins.take(5))
    }

    suspend fun getCoins(query: String): List<Coin> = withTimeout(1100) {
        delay(randomDelay.nextLong(500, 1200))
        allCoins.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun getFavorites(): List<Coin> =
        _favorites.toList()

    suspend fun addFavorite(coin: Coin) {
        delay(randomDelay.nextLong(3000))
        _favorites.add(coin)
    }

    suspend fun removeFavorite(coin: Coin) {
        delay(randomDelay.nextLong(3000))
        _favorites.remove(coin)
    }

    fun isCoinInFavorites(coin: Coin): Boolean =
        _favorites.contains(coin)

    fun getCoinById(coinId: String): Coin = allCoins.find { it.id == coinId }!!

}


