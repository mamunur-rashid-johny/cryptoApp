package com.johny.cryptoApp.crypto.presentation.coin_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johny.cryptoApp.R
import com.johny.cryptoApp.core.presentation.component.ShimmerEffect
import com.johny.cryptoApp.crypto.presentation.coin_details.components.InfoCard
import com.johny.cryptoApp.crypto.presentation.coin_details.components.LineChart
import com.johny.cryptoApp.crypto.presentation.coin_list.CoinListState
import com.johny.cryptoApp.crypto.presentation.coin_list.components.previewCoin
import com.johny.cryptoApp.crypto.presentation.models.toDisplayableNumber
import com.johny.cryptoApp.ui.theme.JCPracticeTheme
import com.johny.cryptoApp.ui.theme.greenBackground


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailsScreen(
    state: CoinListState,
    modifier: Modifier = Modifier
) {

    val contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    if (state.isLoading) {

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            ShimmerEffect(
                modifier = Modifier.fillMaxSize()
            )
        }

    } else if (state.selectedCoinUi != null) {

        val coin = state.selectedCoinUi
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(coin.iconRes),
                contentDescription = coin.name,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = coin.name,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                color = contentColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = coin.symbol,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                color = contentColor,
                textAlign = TextAlign.Center
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                InfoCard(
                    title = stringResource(R.string.market_cap),
                    formattedText = "$ ${coin.marketCapUsd.formatted}",
                    icon = ImageVector.vectorResource(R.drawable.stock)
                )

                InfoCard(
                    title = stringResource(R.string.price),
                    formattedText = "$ ${coin.priceUsd.formatted}",
                    icon = ImageVector.vectorResource(R.drawable.dollar)
                )

                val absoluteChangedFormatted =
                    (coin.priceUsd.value * (coin.changePercent24Hr.value / 100)).toDisplayableNumber()

                val isPositive = coin.changePercent24Hr.value > 0.0

                (if (isPositive) {
                    if (isSystemInDarkTheme()) Color.Green else greenBackground
                } else {
                    MaterialTheme.colorScheme.error
                }).also {

                    InfoCard(
                        title = stringResource(R.string.change_last_24h),
                        formattedText = absoluteChangedFormatted.formatted,
                        icon = ImageVector.vectorResource(if (isPositive) R.drawable.trending else R.drawable.trending_down),
                        contentColor = it
                    )
                }


            }

            AnimatedVisibility(visible = coin.coinPriceHistory.isNotEmpty() ) {

                var selectedDataPoint by remember {
                    mutableStateOf<DataPoint?>(null)
                }

                LineChart(
                    dataPoints = coin.coinPriceHistory,
                    style = ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        selectedColor = MaterialTheme.colorScheme.primary,
                        helperLinesThicknessPx = 5f,
                        axisLinesThicknessPx = 5f,
                        labelFontSize = 14.sp,
                        minYLabelSpacing = 25.dp,
                        verticalPadding = 8.dp,
                        horizontalPadding = 8.dp,
                        xAxisLabelSpacing = 8.dp
                    ) ,
                    visibleDataPointsIndices =coin.coinPriceHistory.indices ,
                    unit = "$",
                    modifier = Modifier.fillMaxSize().aspectRatio(16/9f),
                    selectedDataPoint = selectedDataPoint,
                    onSelectedDataPoint = {
                        selectedDataPoint = it
                    },
                    onCancelDrag = {
                        selectedDataPoint = it
                    }
                )
            }

        }


    }
}

@PreviewLightDark
@Composable
fun CoinDetailsScreenPreview() {
    JCPracticeTheme {
        CoinDetailsScreen(
            state = CoinListState(
                selectedCoinUi = previewCoin
            ),
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background
            )
        )
    }
}