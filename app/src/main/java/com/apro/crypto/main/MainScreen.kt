package com.apro.crypto.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apro.crypto.Navigator
import com.apro.crypto.R
import com.apro.crypto.asMoney
import com.apro.crypto.details.QuestionDialog
import com.apro.crypto.main.models.MainAction
import com.apro.crypto.main.models.MainListState
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navigator: Navigator,
    scaffoldState: ScaffoldState
) {
    BackHandler {
        viewModel.submitAction(MainAction.GoBackPressed)
    }
    val state by viewModel.state.collectAsState()
    var menuOpened by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.List,
                            tint = Color.White,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                menuOpened = true
                            })
                        DropdownMenu(
                            expanded = menuOpened,
                            onDismissRequest = { menuOpened = false }) {
                            TextButton(onClick = { navigator.openSimpleCalculator() }) {
                                Text(text = "Simple calculator")
                            }
                            TextButton(onClick = { navigator.openLoanCalculator() }) {
                                Text(text = "Loan calculator")
                            }
                            TextButton(onClick = {
                                viewModel(MainAction.OpenSortClicked)
                                menuOpened = false
                            }) {
                                Text(text = "Sort order")
                            }
                        }
                    }
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(CenterVertically)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    AnimatedVisibility(visible = state.cartSum > 0) {
                        Text(text = state.cartSum.asMoney(), modifier = Modifier.clickable {
                            navigator.openOrder()
                        })
                    }
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        if (state.closeQuestion) {
            QuestionDialog(
                onOk = {
                    viewModel(MainAction.LeaveAgreed)
                },
                onCancel = {
                    viewModel(MainAction.LeaveDenied)
                }
            )
        }
        Column {
            TextField(
                label = {
                    Text(text = "Looking for a crypto?")
                },
                value = state.searchText,
                onValueChange = {
                    viewModel.submitAction(MainAction.SearchRequest(it, state.sortType))
                },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    Row(verticalAlignment = CenterVertically) {
                        if (state.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        }
                        if (state.searchText.isNotEmpty()) {
                            Image(imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    viewModel(MainAction.SearchRequest("", state.sortType))
                                })
                        }
                    }
                },
                singleLine = true,
                maxLines = 0
            )
            when (val listState = state.listState) {
                is MainListState.Failure -> {
                    FailureButton {
                        viewModel.submitAction(
                            MainAction.SearchRequest(
                                state.searchText,
                                state.sortType
                            )
                        )
                    }
                }
                is MainListState.Items -> {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = state.isLoading),
                        onRefresh = {
                            viewModel(
                                MainAction.SearchRequest(
                                    state.searchText,
                                    state.sortType
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        ItemsList(listState, viewModel)
                    }
                }
            }

        }

    }
}

@Composable
private fun ItemsList(
    list: MainListState.Items,
    viewModel: MainViewModel
) {
    LazyColumn {
        items(list.value) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.submitAction(MainAction.ItemClicked(it.model))
                        }
                        .padding(4.dp),
                    verticalAlignment = CenterVertically
                ) {
                    val coin = it.model
                    Box(
                        modifier = Modifier.size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (it.isInProcess) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(
                                    8.dp
                                )
                            )
                        } else {
                            Image(imageVector = Icons.Default.Check,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .clickable {
                                        viewModel(
                                            MainAction.ToggleFavorite(
                                                it
                                            )
                                        )
                                    }
                                    .alpha(if (it.isFavorite) 1f else 0.3f))
                        }
                    }
                    Image(
                        painter = rememberCoilPainter(
                            coin.image
                        ),
                        contentDescription = "",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = coin.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = coin.price.asMoney())
                        Text(
                            text = coin.change42.asMoney(),
                            color = if (coin.change42 < 0) Color.Red else Color.Green
                        )
                    }
                    IconButton(onClick = {
                        viewModel(MainAction.Buy(coin = coin))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null
                        )
                    }
                }
                Divider()
            }
        }
    }
}

@Composable
fun ColumnScope.FailureButton(onClick: () -> Unit) {
    val errorButtonTransaction = rememberInfiniteTransition()
    val scale by errorButtonTransaction.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    Button(
        onClick = onClick,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(16.dp)
            .scale(scale),
        colors = ButtonDefaults.outlinedButtonColors()
    ) {
        Icon(
            imageVector = Icons.Default.Refresh, contentDescription = "",
            tint = MaterialTheme.colors.error
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Failed! Try again?")
    }
}
