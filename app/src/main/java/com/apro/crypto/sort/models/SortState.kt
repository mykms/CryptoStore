package com.apro.crypto.sort.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SortState(
    val options: List<SortOption> = listOf()
)

data class SortOption(
    val title: String,
    val isSelected: Boolean,
    val sortType: SortType
)

@Parcelize
enum class SortType : Parcelable {
    Name,
    MinToMax,
    MaxToMin
}