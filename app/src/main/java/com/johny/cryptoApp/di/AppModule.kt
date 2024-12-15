package com.johny.cryptoApp.di

import com.johny.cryptoApp.core.data.networking.HttpClientFactory
import com.johny.cryptoApp.crypto.data.networking.RemoteCoinDataSource
import com.johny.cryptoApp.crypto.domain.CoinDataSource
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.johny.cryptoApp.crypto.presentation.coin_list.CoinListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()
    viewModelOf(::CoinListViewModel)
}