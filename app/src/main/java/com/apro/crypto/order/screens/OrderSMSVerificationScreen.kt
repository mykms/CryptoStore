package com.apro.crypto.order.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
        AnimatedVisibility(visible = state.isBusy) {
            CircularProgressIndicator()
        }
        TextField(
            value = state.smsCode,
            onValueChange = {
                viewModel(OrderAction.SmsCodeEntered(it))
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            enabled = state.isBusy.not(),
            modifier = Modifier
                .focusRequester(focusRequester)
        )
        if (state.canResendSms.not() && state.isBusy.not()) {
            Text(text = "Sms can be resent in ${state.secondsToResend} seconds")
        } else {
            AnimatedVisibility(visible = state.isBusy.not()) {
                Button(onClick = {
                    viewModel(OrderAction.Buy())
                }, modifier = Modifier.padding(16.dp)) {
                    Text("Send SMS code")
                }
            }
        }
    }
}