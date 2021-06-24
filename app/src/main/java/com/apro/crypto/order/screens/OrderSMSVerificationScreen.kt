package com.apro.crypto.order.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.apro.crypto.order.OrderViewModel
import com.apro.crypto.order.models.OrderAction
import com.apro.crypto.order.models.OrderState
import com.apro.crypto.order.models.canResendSms


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OrderSMSVerificationScreen(
    state: OrderState,
    viewModel: OrderViewModel,
    focusRequester: FocusRequester
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        TextField(
            value = state.smsCode,
            onValueChange = {
                viewModel(OrderAction.SmsCodeEntered(it))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            enabled = state.isBusy.not(),
            modifier = Modifier.focusRequester(focusRequester),
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(text = "Enter verification code")
            },
            trailingIcon = {
                when {
                    state.isBusy -> CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                    state.canResendSms.not() ->
                        Text(text = "${state.secondsToResend} s")
                    else ->
                        IconButton(onClick = {
                            viewModel(OrderAction.Buy())
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }

                }
            }
        )

    }
}