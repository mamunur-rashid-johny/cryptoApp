package com.johny.cryptoApp.crypto.data.networking

import com.johny.cryptoApp.core.data.networking.constructUrl
import com.johny.cryptoApp.core.data.networking.safeCall
import com.johny.cryptoApp.core.domain.utils.NetworkError
import com.johny.cryptoApp.core.domain.utils.Result
import com.johny.cryptoApp.core.domain.utils.map
import com.johny.cryptoApp.crypto.data.mapper.toCoin
import com.johny.cryptoApp.crypto.data.mapper.toCoinPrice
import com.johny.cryptoApp.crypto.data.networking.dto.CoinHistoryDto
import com.johny.cryptoApp.crypto.data.networking.dto.CoinResponseDto
import com.johny.cryptoApp.crypto.domain.Coin
import com.johny.cryptoApp.crypto.domain.CoinDataSource
import com.johny.cryptoApp.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource(
    private val httpClient: HttpClient
) : CoinDataSource {
    override suspend fun getCoin(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinPriceHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMills = start.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val endMills = end.withZoneSameInstant(ZoneId.of("UTC")).toInstant().toEpochMilli()
        return safeCall<CoinHistoryDto> {
            httpClient.get(
                urlString = constructUrl("/assets/$coinId/history")
            ) {
                parameter("interval", "h6")
                parameter("start", startMills)
                parameter("end", endMills)
            }
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }

}