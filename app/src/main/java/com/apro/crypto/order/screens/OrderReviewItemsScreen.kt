package com.apro.crypto.order.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apro.crypto.asMoney
import com.apro.crypto.order.OrderViewModel
import com.apro.crypto.order.models.OrderAction
import com.apro.crypto.order.models.OrderItem
import com.apro.crypto.order.models.OrderState
import com.apro.crypto.order.models.total

@Composable
fun OrderReviewItemsScreen(state: OrderState, viewModel: OrderViewModel) {
    Column() {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.items, key = { it.key() }) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = it.coin.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${it.amount} x " + it.total.asMoney())
                    IconButton(onClick = { viewModel(OrderAction.RemoveItem(it.coin)) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                }
                Divider()
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = state.total.asMoney())
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel(OrderAction.Buy()) }) {
                Text(text = "Order")
            }
        }
    }
}

private fun OrderItem.key(): String =
    listOf(coin.name, amount.toString()).joinToString()
