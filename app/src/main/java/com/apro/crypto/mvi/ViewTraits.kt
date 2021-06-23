package com.apro.crypto.mvi

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.apro.crypto.Navigator
import com.apro.crypto.createNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

interface ViewTraits {
    val context: Context
    val navigator: Navigator
    fun handleOnBackPressed(block: () -> Unit)
}

fun <S, VM : MVIEngine<S, *, *>> Fragment.buildView(
    viewModel: VM,
    onLaunch: CoroutineScope.(ViewTraits) -> Unit = {},
    onEvent: suspend CoroutineScope.(traits: ViewTraits, event: Event) -> Unit = { _, _ -> },
    content: @Composable ViewTraits.(VM) -> Unit
): ComposeView =
    ComposeView(requireContext()).apply {
        val fragment = this@buildView
        val traits = object : ViewTraits {
            override val context: Context get() = fragment.requireContext()
            override val navigator: Navigator = fragment.requireActivity().createNavigator()
            override fun handleOnBackPressed(block: () -> Unit) {
                fragment.requireActivity().onBackPressedDispatcher.addCallback(
                    fragment,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            block()
                        }
                    })
            }
        }
        setContent {
            LaunchedEffect(key1 = this@buildView) {
                onLaunch(this, traits)
                viewModel.events.collect {
                    onEvent(traits, it)
                }
            }
            MaterialTheme {
                traits.content(viewModel)
            }
        }
    }
