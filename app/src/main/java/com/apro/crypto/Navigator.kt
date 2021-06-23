package com.apro.crypto

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import com.apro.crypto.sort.ChooseSortBottomSheet
import com.apro.crypto.sort.models.SortType

interface Navigator {
    fun openDetails(bundle: Bundle)
    fun onBackPressed()
    fun openOrder()
    fun openSortBottomSheet(sortType: SortType)
    fun openCalc()
    fun openLoanCalc()
}

fun createNavigator(activity: MainActivity): Navigator =
    object : Navigator {
        override fun onBackPressed() {
            if (activity.supportFragmentManager.backStackEntryCount > 0)
                activity.supportFragmentManager.popBackStack()
            else
                activity.finish()
        }

        override fun openDetails(bundle: Bundle) {
            activity.navigateToDetails(bundle)
        }

        override fun openOrder() {
            activity.navigateToOrder()
        }

        override fun openSortBottomSheet(sortType: SortType) {
            ChooseSortBottomSheet().apply {
                arguments = bundleOf(ChooseSortBottomSheet.SORT_RESULT_BUNDLE_KEY to sortType)
            }.show(activity.supportFragmentManager, "SORT_BOTTOM_SHEEP")
        }

        override fun openCalc() {
            activity.navigateToCalc()
        }

        override fun openLoanCalc() {
            activity.navigateToLoanCalc()
        }
    }

fun Activity.createNavigator() = createNavigator(this as MainActivity)