package com.apro.crypto.order.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apro.crypto.order.OrderViewModel
import com.apro.crypto.order.models.OrderAction
import com.apro.crypto.order.models.OrderState

@Composable
fun OrderSuccessScreen(state: OrderState, viewModel: OrderViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your order was successful!")
        Button(
            onClick = { viewModel(OrderAction.GoBackPressed(state.screen)) },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.outlinedButtonColors(),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Go to the shop!")
        }
    }
}