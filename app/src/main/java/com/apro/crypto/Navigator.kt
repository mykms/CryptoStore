package com.apro.crypto

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.apro.crypto.calc.CalcFragment
import com.apro.crypto.details.DetailsFragment
import com.apro.crypto.loan.LoanFragment
import com.apro.crypto.order.OrderFragment
import com.apro.crypto.sort.ChooseSortBottomSheet
import com.apro.crypto.sort.models.SortType

interface Navigator {
    fun openDetails(bundle: Bundle)
    fun onBackPressed()
    fun openOrder()
    fun openSortBottomSheet(sortType: SortType)
    fun openSimpleCalculator()
    fun openLoanCalculator()
}

fun createNavigator(activity: MainActivity): Navigator =
    object : Navigator {
        val fm = activity.supportFragmentManager
        override fun onBackPressed() {
            if (fm.backStackEntryCount > 0)
                fm.popBackStack()
            else
                activity.finish()
        }

        override fun openDetails(bundle: Bundle) {
            fm.commit {
                replace(R.id.fragmentContainer, DetailsFragment().apply {
                    arguments = bundle
                }).addToBackStack("details")
            }
        }

        override fun openOrder() {
            fm.commit {
                replace(R.id.fragmentContainer, OrderFragment()).addToBackStack("order")
            }
        }

        override fun openSortBottomSheet(sortType: SortType) {
            ChooseSortBottomSheet().apply {
                arguments = bundleOf(ChooseSortBottomSheet.SORT_RESULT_BUNDLE_KEY to sortType)
            }.show(fm, "SORT_BOTTOM_SHEEP")
        }

        override fun openSimpleCalculator() {
            fm.commit {
                replace(R.id.fragmentContainer, CalcFragment()).addToBackStack("calc")
            }
        }

        override fun openLoanCalculator() {
            fm.commit {
                replace(R.id.fragmentContainer, LoanFragment()).addToBackStack("loan")
            }
        }
    }

fun Activity.createNavigator() = createNavigator(this as MainActivity)