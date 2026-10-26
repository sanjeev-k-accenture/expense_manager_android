package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.expensemanager.R
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.common.ui.EmptyView
import com.example.expensemanager.common.ui.ErrorView
import com.example.expensemanager.common.ui.LoadingView
import com.example.expensemanager.feature.transaction.presentation.navigation.TransactionDestination
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiState
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.screen.view.TransactionHomeView
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util.transactionHistoryListForPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TransactionHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiStateFlow: StateFlow<TransactionHomeUiState>,
    handleUiAction: (TransactionHomeUiAction) -> Unit,
) {
    val uiState = uiStateFlow.collectAsStateWithLifecycle()

    when (val currUiState = uiState.value) {
        TransactionHomeUiState.Empty -> EmptyView(
            modifier = modifier,
            message = stringResource(R.string.no_items),
            buttonText = stringResource(R.string.add_transaction),
            onButtonClick = {
                navController.navigate(route = TransactionDestination.Form.route)
            })

        is TransactionHomeUiState.Error ->
            ErrorView(
                modifier = modifier,
                message = currUiState.message,
                onTryAgainClicked = {
                    handleUiAction(TransactionHomeUiAction.FetchTransactions)
                }
            )

        TransactionHomeUiState.Loading -> LoadingView()

        is TransactionHomeUiState.Success -> TransactionHomeView(
            modifier = modifier,
            transactions = currUiState.transactionHistoryList,
            onAddTxnBtnClick = {
                navController.navigate(route = TransactionDestination.Form.route)
            },
            onTransactionClick = { id ->
                navController.navigate(
                    route = TransactionDestination.Detail.createRoute(id = id)
                )
            }
        )
    }

}

@PreviewWithUi
@Composable
private fun TransactionHomeScreenPreview1() {
    TransactionHomeScreen(
        uiStateFlow = MutableStateFlow(TransactionHomeUiState.Loading),
        handleUiAction = {},
        navController = NavController(
            LocalContext.current
        )
    )
}

@PreviewWithUi
@Composable
private fun TransactionHomeScreenPreview2() {
    TransactionHomeScreen(
        uiStateFlow = MutableStateFlow(TransactionHomeUiState.Empty), handleUiAction = {},
        navController = NavController(
            LocalContext.current
        )
    )
}

@PreviewWithUi
@Composable
private fun TransactionHomeScreenPreview3() {
    TransactionHomeScreen(
        uiStateFlow = MutableStateFlow(TransactionHomeUiState.Error()), handleUiAction = {},
        navController = NavController(
            LocalContext.current
        )
    )
}

@PreviewWithUi
@Composable
private fun TransactionHomeScreenPreview4() {
    TransactionHomeScreen(
        uiStateFlow = MutableStateFlow(
            TransactionHomeUiState.Success(
                transactionHistoryList = transactionHistoryListForPreview
            )
        ), handleUiAction = {},
        navController = NavController(
            LocalContext.current
        )
    )
}
