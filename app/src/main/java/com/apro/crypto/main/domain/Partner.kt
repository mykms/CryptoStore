package com.apro.crypto.main.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Partner(
    val name: String,
    val advantage: String
) : Parcelable