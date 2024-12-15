package com.johny.cryptoApp.crypto.presentation.coin_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.johny.cryptoApp.core.presentation.component.ShimmerEffect
import com.johny.cryptoApp.crypto.presentation.coin_list.components.CoinListItem
import com.johny.cryptoApp.crypto.presentation.coin_list.components.previewCoin
import com.johny.cryptoApp.ui.theme.JCPracticeTheme

@Composable
fun CoinListScreen(
    coinListState: CoinListState,
    onAction:(CoinListAction) -> Unit,
    modifier: Modifier = Modifier
) {



    if (coinListState.isLoading) {

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            ShimmerEffect(
                modifier = Modifier.fillMaxSize()
            )
        }

    } else {

        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(coinListState.coins) { coinUi ->

                CoinListItem(
                    coinUi = coinUi,
                    onClick = {
                        onAction(CoinListAction.OnCoinClick(coinUi))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider()
            }

        }

    }
}

@PreviewLightDark
@Composable
private fun CoinListScreenPreview() {
    JCPracticeTheme {
        CoinListScreen(
            coinListState = CoinListState(
                coins = (1..100).map { previewCoin.copy(id = it.toString()) }
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            onAction = {}
        )
    }
}