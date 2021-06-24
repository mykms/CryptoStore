package com.apro.crypto.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.focus.FocusRequester
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.apro.crypto.main.models.GoBackEvent
import com.apro.crypto.main.models.KeyboardClose
import com.apro.crypto.main.models.RequestFocus
import com.apro.crypto.main.models.ShowToast
import com.apro.crypto.mvi.buildView
import com.apro.crypto.order.models.OrderAction
import com.apro.crypto.order.models.OrderEvent
import com.apro.crypto.order.screens.OrderScreen
import com.apro.crypto.showToast
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class OrderFragment : Fragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()
    private val viewModel: OrderViewModel by viewModel()
    private val focusRequester = FocusRequester()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        buildView(
            viewModel = viewModel,
            onLaunch = {
                it.handleOnBackPressed {
                    viewModel(OrderAction.GoBackPressed(viewModel.state.value.screen))
                }
            },
            onEvent = { traits, event ->
                when (event) {
                    is OrderEvent.FinishPlacing -> {
                        viewModel(OrderAction.Buy(isFinished = true))
                    }
                    is GoBackEvent -> traits.navigator.onBackPressed()
                    is ShowToast -> showToast(event.text)
                    is RequestFocus -> focusRequester.requestFocus()
                    is KeyboardClose -> {
                        val input = requireContext().getSystemService<InputMethodManager>()
                        input?.hideSoftInputFromWindow(requireView().windowToken, 0)
                    }
                }
            }
        ) {
            OrderScreen(viewModel = it, focusRequester)
        }
}