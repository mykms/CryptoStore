package com.apro.crypto.main.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coin(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("image")
    var image: String,
    @SerialName("current_price")
    val price: Double,
    @SerialName("price_change_24h")
    var change42: Double,
    @SerialName("market_cap")
    val marketCap: Double,
    @SerialName("market_cap_rank")
    val rank: Int
)