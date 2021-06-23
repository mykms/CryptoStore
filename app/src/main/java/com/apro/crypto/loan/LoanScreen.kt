package com.apro.crypto.loan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apro.crypto.R
import com.apro.crypto.asMoney
import com.apro.crypto.mvi.ViewTraits

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ViewTraits.LoanScreen(viewModel: LoanViewModel) {
    val state by viewModel.state.collectAsState()
    val backgroundColor by animateColorAsState(
        targetValue = if (state.isError)
            Color.Red.copy(alpha = 0.5f)
        else
            Color.LightGray.copy(alpha = 0.2f)
    )
    Scaffold(
        topBar = {
            TopAppBar {
                IconButton(onClick = {
                    navigator.onBackPressed()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                Text(text = "Loan calculator", fontWeight = FontWeight.Bold)
            }
        }
    ) {
        Card(
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(backgroundColor)
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                SearchTextBox(
                    value = state.amountOfMoney,
                    onValueChange = { viewModel(LoanAction.AmountChanged(amountOfMoney = it)) },
                    invertCorners = state.termOfMonths > 8
                )
                Slider(
                    value = state.percentOfMax,
                    onValueChange = { viewModel(LoanAction.PercentOfChanged(it)) }
                )
                TermsView(
                    selected = state.termOfMonths,
                    onSelect = { viewModel(LoanAction.TermsChanged(it)) }
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                CaptionWithValue(
                    caption = "Month payment",
                    value = state.monthlyPayment,
                    modifier = Modifier.align(Alignment.End),
                    converter = { asMoney() }
                )
                CaptionWithValue(
                    caption = "Interest rate",
                    value = state.interestRate.toDouble(),
                    modifier = Modifier.align(Alignment.End),
                    converter = { "${toInt()}%" }
                )
                AnimatedVisibility(visible = state.percentOfMax > 0.8 && state.termOfMonths == 12) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "!!!Special deal!!!",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.rope),
                            contentDescription = null,
                            modifier = Modifier
                                .size(180.dp)
                                .border(2.dp, Color.LightGray)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CaptionWithValue(
    caption: String,
    value: Double,
    modifier: Modifier,
    converter: Double.() -> String
) {
    val animatedValue by animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(1000)
    )
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$caption:", fontWeight = FontWeight.Black, color = Color.LightGray)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = animatedValue.toDouble().converter(),
            fontWeight = FontWeight.Black,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun SearchTextBox(value: String, onValueChange: (String) -> Unit, invertCorners: Boolean) {
    val corners by animateDpAsState(
        targetValue = if (invertCorners) 0.dp else 24.dp
    )
    val cornersInverted by animateDpAsState(
        targetValue = if (invertCorners) 24.dp else 0.dp
    )
    val shape = RoundedCornerShape(
        topStart = corners,
        bottomEnd = corners,
        bottomStart = cornersInverted,
        topEnd = cornersInverted
    )
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle.Default.copy(fontSize = 24.sp, background = Color.LightGray),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .background(Color.LightGray, shape)
            .border(2.dp, Color.DarkGray, shape)
            .padding(16.dp),
        maxLines = 1
    )
}

@Composable
private fun TermsView(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (value in 6..12 step 2) {
            val background by animateColorAsState(
                targetValue = if (value == selected) MaterialTheme.colors.primary else Color.Transparent,
                animationSpec = tween(2000)
            )
            val textColor by animateColorAsState(
                targetValue = if (value != selected) MaterialTheme.colors.primary else Color.White,
                animationSpec = tween(2000)
            )
            Text(
                text = value.toString(),
                textAlign = TextAlign.Center,
                color = textColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .border(1.dp, Color.DarkGray)
                    .background(background)
                    .clickable {
                        onSelect(value)
                    }
                    .padding(4.dp)
            )
        }
    }
}