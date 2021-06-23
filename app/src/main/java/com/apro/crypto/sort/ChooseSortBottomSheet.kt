package com.apro.crypto.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.apro.crypto.main.models.OpenSortResult
import com.apro.crypto.mvi.buildView
import com.apro.crypto.sort.models.SortAction
import com.apro.crypto.sort.models.SortType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class ChooseSortBottomSheet : BottomSheetDialogFragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()

    private val viewModel: SortViewModel by viewModel {
        parametersOf(checkNotNull(requireArguments().getParcelable<SortType>(SORT_RESULT_BUNDLE_KEY)))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = buildView(
        viewModel = viewModel,
        onEvent = { _, event ->
            when (event) {
                is OpenSortResult -> {
                    setFragmentResult(
                        SORT_RESULT, bundleOf(
                            SORT_RESULT_BUNDLE_KEY to event.sortType
                        )
                    )
                    this@ChooseSortBottomSheet.dismiss()
                }
            }
        }
    ) {
        SortScreen(viewModel = it)
    }

    companion object {
        const val SORT_RESULT = "SORT_RESULT"
        const val SORT_RESULT_BUNDLE_KEY = "SORT_RESULT_BUNDLE_KEY"
    }
}

@Composable
fun SortScreen(viewModel: SortViewModel) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        state.options.forEach { option ->
            Text(
                text = option.title,
                fontWeight = FontWeight.Bold.takeIf { option.isSelected },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel(SortAction.Select(option))
                    }
                    .padding(16.dp)
            )
        }
    }
}