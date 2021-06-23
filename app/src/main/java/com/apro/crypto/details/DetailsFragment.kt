package com.apro.crypto.details

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apro.crypto.getArgs
import com.apro.crypto.mvi.buildView
import kotlinx.parcelize.Parcelize
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

@Parcelize
class DetailsArgs(val coinId: String) : Parcelable

class DetailsFragment : Fragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args: DetailsArgs = getArgs()
        val viewModel = getViewModel<DetailsViewModel> { parametersOf(args) }
        return buildView(viewModel, content = { DetailsScreen(it) })
    }

}
