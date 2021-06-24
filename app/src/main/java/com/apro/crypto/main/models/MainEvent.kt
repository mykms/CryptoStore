package com.apro.crypto.main.models

import android.os.Bundle
import com.apro.crypto.main.domain.Coin
import com.apro.crypto.mvi.Event
import com.apro.crypto.sort.models.SortType

class ShowToast(val text: String) : Event
class ShowSnackbar(val message: String, val coin: Coin? = null) : Event
object GoBackEvent : Event
object RequestFocus : Event
object KeyboardClose : Event
object OpenSortEvent : Event
class OpenSortResult(val sortType: SortType) : Event
class OpenDetails(val bundle: Bundle) : Event
