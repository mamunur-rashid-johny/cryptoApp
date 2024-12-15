package com.johny.cryptoApp.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.johny.cryptoApp.core.presentation.utils.ObserveAsEvent
import com.johny.cryptoApp.core.presentation.utils.toString
import com.johny.cryptoApp.crypto.presentation.coin_details.CoinDetailsScreen
import com.johny.cryptoApp.crypto.presentation.coin_list.CoinListAction
import com.johny.cryptoApp.crypto.presentation.coin_list.CoinListEvent
import com.johny.cryptoApp.crypto.presentation.coin_list.CoinListScreen
import com.johny.cryptoApp.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveCoinDetailPane(
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvent(events = viewModel.event) { event ->
        when (event) {
            is CoinListEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CoinListScreen(
                    coinListState = state,
                    onAction = {action ->
                        viewModel.onAction(action)
                        when(action){
                            is CoinListAction.OnCoinClick ->{
                                navigator.navigateTo(
                                    pane = ListDetailPaneScaffoldRole.Detail
                                )
                            }
                            else ->{

                            }
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                CoinDetailsScreen(state = state)
            }
        },
        modifier = modifier
    )

}