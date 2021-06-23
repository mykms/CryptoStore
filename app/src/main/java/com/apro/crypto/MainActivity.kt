package com.apro.crypto

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.apro.crypto.calc.CalcFragment
import com.apro.crypto.details.DetailsFragment
import com.apro.crypto.loan.LoanFragment
import com.apro.crypto.main.MainFragment
import com.apro.crypto.order.OrderFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, MainFragment())
            }
        }
    }

    fun navigateToDetails(bundle: Bundle) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, DetailsFragment().apply {
                arguments = bundle
            }).addToBackStack("details")
        }
    }

    fun navigateToOrder() {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, OrderFragment()).addToBackStack("order")
        }
    }

    fun navigateToCalc() {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, CalcFragment()).addToBackStack("calc")
        }
    }

    fun navigateToLoanCalc() {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, LoanFragment()).addToBackStack("loan")
        }
    }
}



