package com.apro.crypto.order.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apro.crypto.order.OrderMiddleware
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
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
        Box(modifier = Modifier.align(TopEnd)) {
            val screenWidth = LocalView.current.width
            val screenHeight = LocalView.current.height
            AnimatedVisibility(
                visible = state.canResendSms.not(),
                enter = slideInHorizontally(initialOffsetX = {
                    it - screenWidth
                }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = {
                    it + screenHeight
                }) + fadeOut()
            ) {
                val shape = RoundedCornerShape(16.dp)
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Black, shape = shape)
                        .border(2.dp, Color.Gray, shape = shape)
                        .padding(8.dp)
                        .size(80.dp),
                    contentAlignment = Center
                ) {
                    repeat(OrderMiddleware.MAX_SECONDS.inc()) {
                        Column {
                            AnimatedVisibility(
                                visible = it == state.secondsToResend
                            ) {
                                Text(
                                    text = it.toString(),
                                    fontSize = 42.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}