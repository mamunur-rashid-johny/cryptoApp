package com.johny.cryptoApp.crypto.presentation.coin_list

import com.johny.cryptoApp.crypto.presentation.models.CoinUi

sealed interface CoinListAction {
    data class OnCoinClick(val coinUi: CoinUi) : CoinListAction
    data object OnCoinListRefresh : CoinListAction
}