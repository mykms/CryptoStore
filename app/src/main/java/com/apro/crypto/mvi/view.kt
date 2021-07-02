package com.apro.crypto.mvi

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

fun <S, VM : MVIEngine<S, *, *>> Fragment.buildView(
    viewModel: VM,
    onLaunch: CoroutineScope.() -> Unit = {},
    onEvent: suspend CoroutineScope.(event: Event) -> Unit = { },
    content: @Composable () -> Unit
): ComposeView =
    ComposeView(requireContext()).apply {
        setContent {
            LaunchedEffect(key1 = this@buildView) {
                onLaunch(this)
                viewModel.events.collect {
                    onEvent(it)
                }
            }
            MaterialTheme {
                content()
            }
        }
    }
