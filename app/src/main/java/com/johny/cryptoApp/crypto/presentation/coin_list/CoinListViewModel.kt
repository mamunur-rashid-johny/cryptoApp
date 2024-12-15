package com.johny.cryptoApp.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johny.cryptoApp.core.domain.utils.onError
import com.johny.cryptoApp.core.domain.utils.onSuccess
import com.johny.cryptoApp.crypto.domain.CoinDataSource
import com.johny.cryptoApp.crypto.presentation.coin_details.DataPoint
import com.johny.cryptoApp.crypto.presentation.models.CoinUi
import com.johny.cryptoApp.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state
        .onStart {
            loadCoinList()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinListState()
        )

    private val _event = Channel<CoinListEvent>()
    val event = _event.receiveAsFlow()


    private fun loadCoinList() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(isLoading = true)
            }

            coinDataSource
                .getCoin()
                .onSuccess { coins ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            coins = coins.map { coin -> coin.toCoinUi() }
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _event.trySend(CoinListEvent.Error(error = error))
                }

        }
    }

    fun onAction(coinListAction: CoinListAction){
        when(coinListAction){
            is CoinListAction.OnCoinClick -> {
                selectCoin(coinListAction.coinUi)
            }
            CoinListAction.OnCoinListRefresh -> {
                loadCoinList()
            }
        }
    }

    private fun selectCoin(coinUi: CoinUi){
        _state.update { it.copy(selectedCoinUi = coinUi) }

        viewModelScope.launch(Dispatchers.IO) {
            coinDataSource
                .getCoinPriceHistory(
                    coinId = coinUi.id,
                    start = ZonedDateTime.now().minusDays(5) ,
                    end = ZonedDateTime.now()
                ).onSuccess { history ->
                    val dataPoint = history
                        .sortedBy {
                            it.dateTime
                        }
                        .map {
                            DataPoint(
                                x = it.dateTime.hour.toFloat(),
                                y = it.priceUsd.toFloat(),
                                xLabel = DateTimeFormatter.ofPattern("ha\nM/d").format(it.dateTime)
                            )

                    }

                    _state.update {
                        it.copy(
                            selectedCoinUi = it.selectedCoinUi?.copy(coinPriceHistory = dataPoint)
                        )
                    }

                }.onError { error ->
                    _event.trySend(CoinListEvent.Error(error = error))
                }
        }
    }
}