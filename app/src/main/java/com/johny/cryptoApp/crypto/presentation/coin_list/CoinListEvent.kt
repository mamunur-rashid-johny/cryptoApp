package com.johny.cryptoApp.crypto.presentation.coin_list

import com.johny.cryptoApp.core.domain.utils.NetworkError

sealed interface CoinListEvent {
    data class Error(val error: NetworkError) : CoinListEvent
}