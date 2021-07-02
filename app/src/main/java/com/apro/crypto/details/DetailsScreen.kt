package com.apro.crypto.details

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apro.crypto.asMoney
import com.apro.crypto.details.models.DetailsAction
import com.apro.crypto.details.models.calcText
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlin.math.roundToInt


@Composable
fun DetailsScreen(viewModel: DetailsViewModel, backPressedDispatcher: OnBackPressedDispatcher) {
    val state by viewModel.state.collectAsState()
    val coin = state.coin
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        backPressedDispatcher.onBackPressed()
                    }) {
                        Image(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                title = {
                    Text(text = "Crypto: ${coin?.name}", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {
                        backPressedDispatcher.onBackPressed()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "")
                    }
                })

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            if (coin != null) {
                Image(
                    painter = rememberCoilPainter(request = coin.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                        .alpha(0.3f)
                        .padding(16.dp)
                )

                Text(
                    text = coin.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(15.dp)
                        .border(width = 2.dp, color = Color.Black.copy(alpha = 0.5f))
                        .background(Color.White.copy(alpha = 0.3f))
                        .padding(8.dp)
                )
                Slider(value = state.amount, valueRange = 1f..100f, onValueChange = { value ->
                    viewModel(DetailsAction.AmountChanged(coin, value))
                }, modifier = Modifier.fillMaxWidth())
                Text(
                    text = "${state.amount.roundToInt()} x ${coin.price.asMoney()} = ${state.calcText}",
                    modifier = Modifier.placeholder(
                        visible = state.calculating,
                        highlight = PlaceholderHighlight.shimmer()
                    )
                )
                Text(
                    text = "Rank: ${coin.rank}",
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "24 hours change: ${coin.change42.asMoney()}",
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Capitalization: ${coin.marketCap.asMoney()}",
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun QuestionDialog(onOk: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onCancel()
        },
        title = {
            Text(text = "Question")
        },
        text = {
            Text("Wanna leave?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onOk()
                }) {
                Text("Ok")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onCancel()
                }) {
                Text("No")
            }
        }
    )
}