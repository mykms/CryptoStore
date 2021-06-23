package com.apro.crypto.loan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apro.crypto.mvi.buildView
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class LoanFragment : Fragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()

    private val viewModel: LoanViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = buildView(
        viewModel = viewModel,
    ) {
        LoanScreen(viewModel = viewModel)
    }

}