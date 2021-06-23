package com.apro.crypto.calc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apro.crypto.mvi.buildView
import com.apro.crypto.showToast
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class CalcFragment : Fragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()
    private val viewModel: CalcViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = buildView(
        viewModel = viewModel,
        onEvent = { _, event ->
            when (event) {
                is CalcEvent.ErrorEvent -> showToast(event.message)
            }
        }
    ) {
        CalcScreen(viewModel = viewModel)
    }

}
