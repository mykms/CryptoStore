package com.apro.crypto.calc

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apro.crypto.mvi.ViewTraits

@Composable
fun ViewTraits.CalcScreen(viewModel: CalcViewModel) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "Calculator", modifier = Modifier.padding(start = 8.dp))
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { navigator.onBackPressed() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalcField(
                value = state.valueX,
                setValue = { viewModel(CalcAction.ValueXChanged(it)) })

            CalcField(
                value = state.valueY,
                setValue = { viewModel(CalcAction.ValueYChanged(it)) },
            )
            ResultView(value = state.result)
            when (state.isCalculating) {
                true -> CircularProgressIndicator()
                false -> CalcButton(onClick = {
                    viewModel(
                        CalcAction.Calculate(
                            x = state.valueX,
                            y = state.valueY
                        )
                    )
                })
            }
        }
    }
}

@Composable
fun CalcField(value: String, setValue: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = setValue,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CalcButton(onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Calculate")
    }
}

@Composable
fun ResultView(value: String) {
    Column {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .defaultMinSize(minWidth = 100.dp)
                .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )
    }

}