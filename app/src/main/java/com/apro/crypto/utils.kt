package com.apro.crypto

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.util.*


const val DEFAULT_ARG = "DEFAULT_ARG"
fun <T : Parcelable> Fragment.getArgs(): T =
    checkNotNull(requireArguments().getParcelable(DEFAULT_ARG))

fun Parcelable.toArgs(): Bundle = bundleOf(DEFAULT_ARG to this)


private val formatMoney: NumberFormat = NumberFormat.getCurrencyInstance().apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
    currency = Currency.getInstance("USD")
}

private val formatInt: NumberFormat = NumberFormat.getCurrencyInstance().apply {
    minimumFractionDigits = 0
    maximumFractionDigits = 0
}

fun Double.asMoney(): String {
    return formatMoney.format(this)
}

fun Int.formatted(): String = formatInt.format(this).drop(1)

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}