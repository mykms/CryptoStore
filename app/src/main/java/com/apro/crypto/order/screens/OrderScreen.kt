package com.apro.crypto.order.screens

import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import com.apro.crypto.order.OrderViewModel
import com.apro.crypto.order.models.OrderAction
import com.apro.crypto.order.models.OrderStateScreen
import com.apro.crypto.order.models.titleId

@Composable
fun OrderScreen(viewModel: OrderViewModel, focusRequester: FocusRequester) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel(OrderAction.GoBackPressed(state.screen))
                    }) {
                        Image(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = state.titleId))
                },
                actions = {
                    IconButton(onClick = {
                        viewModel(OrderAction.GoBackPressed(state.screen))
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "")
                    }
                })
        }) {
        when (state.screen) {
            OrderStateScreen.REVIEW_ITEMS -> OrderReviewItemsScreen(
                state = state,
                viewModel = viewModel
            )
            OrderStateScreen.SMS_VERIFICATION -> OrderSMSVerificationScreen(
                state = state,
                viewModel = viewModel,
                focusRequester = focusRequester
            )
            OrderStateScreen.SUCCESS -> OrderSuccessScreen(state, viewModel)
        }
    }

}