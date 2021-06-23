package com.apro.crypto.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.apro.crypto.main.models.*
import com.apro.crypto.mvi.buildView
import com.apro.crypto.showToast
import com.apro.crypto.sort.ChooseSortBottomSheet
import com.apro.crypto.sort.models.SortType
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainFragment : Fragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        handleSortResult()
        var scaffoldState: ScaffoldState? = null
        return buildView(
            viewModel = viewModel,
            onLaunch = { traits ->
                traits.handleOnBackPressed {
                    viewModel.submitAction(MainAction.GoBackPressed)
                }
            },
            onEvent = { traits, event ->
                when (event) {
                    is ShowToast -> showToast(event.text)
                    is OpenDetails -> traits.navigator.openDetails(event.bundle)
                    is GoBackEvent -> traits.navigator.onBackPressed()
                    is OpenSortEvent -> {
                        val state = viewModel.state.value
                        traits.navigator.openSortBottomSheet(state.sortType)
                    }
                    is ShowSnackbar -> {
                        scaffoldState?.apply {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            launch {
                                val result = snackbarHostState.showSnackbar(event.message, "Undo")
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel(MainAction.UndoBuy(checkNotNull(event.coin)))
                                }
                            }
                        }
                    }
                }
            },
            content = { traits ->
                MainScreen(traits) {
                    scaffoldState = it
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel(MainAction.RefreshCart)
    }

    private fun handleSortResult() {
        setFragmentResultListener(ChooseSortBottomSheet.SORT_RESULT) { _, bundle ->
            bundle.getParcelable<SortType>(ChooseSortBottomSheet.SORT_RESULT_BUNDLE_KEY)?.let {
                val state = viewModel.state.value
                viewModel(MainAction.SearchRequest(state.searchText, it))
            }
        }
    }

}