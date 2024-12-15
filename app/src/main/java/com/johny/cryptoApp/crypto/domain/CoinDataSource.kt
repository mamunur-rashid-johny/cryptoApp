package com.johny.cryptoApp.crypto.domain

import com.johny.cryptoApp.core.domain.utils.NetworkError
import com.johny.cryptoApp.core.domain.utils.Result
import java.time.ZonedDateTime

interface CoinDataSource {
    suspend fun getCoin(): Result<List<Coin>, NetworkError>
    suspend fun getCoinPriceHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError>
}