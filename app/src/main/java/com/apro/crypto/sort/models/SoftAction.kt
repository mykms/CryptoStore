package com.apro.crypto.sort.models

sealed interface SortAction {
    class Select(val option: SortOption) : SortAction
}