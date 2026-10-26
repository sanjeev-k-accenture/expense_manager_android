package com.example.expensemanager.feature.transaction.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensemanager.R
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.screen.TransactionDetailScreen
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.viewmodel.TransactionDetailViewModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.screen.TransactionFormScreen
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.viewmodel.TransactionFormViewModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.screen.TransactionHomeScreen
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.viewmodel.TransactionHomeViewModel

@Composable
fun TransactionNavigation(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = TransactionDestination.Home.route
    ) {
        composable(route = TransactionDestination.Home.route) { backStackEntry ->
            val viewModel: TransactionHomeViewModel = hiltViewModel()

            val resultHandle = backStackEntry.savedStateHandle
            val formResult =
                resultHandle.get<Boolean>(key = TransactionDestination.Form.SAVE_SUCCESSFUL)
                    ?: false
            val saveSuccessMessage = stringResource(R.string.save_transaction_success)

            val detailResult =
                resultHandle.get<Boolean>(key = TransactionDestination.Detail.DELETE_SUCCESSFUL)
                    ?: false
            val deleteSuccessMessage = stringResource(R.string.delete_transaction_success)

            LaunchedEffect(formResult, detailResult) {
                if (formResult) {
                    snackbarHostState.showSnackbar(message = saveSuccessMessage)
                    resultHandle.set(key = TransactionDestination.Form.SAVE_SUCCESSFUL, null)
                }
                if (detailResult) {
                    snackbarHostState.showSnackbar(message = deleteSuccessMessage)
                    resultHandle.set(key = TransactionDestination.Detail.DELETE_SUCCESSFUL, null)
                }
            }

            TransactionHomeScreen(
                modifier = modifier,
                navController = navController,
                uiStateFlow = viewModel.uiState,
                handleUiAction = viewModel::handleUiAction,
            )
        }

        composable(route = TransactionDestination.Form.route) {
            val viewModel: TransactionFormViewModel = hiltViewModel()
            TransactionFormScreen(
                modifier = modifier,
                uiStateFlow = viewModel.formState,
                uiEventFlow = viewModel.eventFlow,
                onFormUpdated = viewModel::updateForm,
                onSaveTransaction = viewModel::onSaveTransaction,
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }

        composable(
            route = TransactionDestination.Detail.route, arguments = listOf(
                navArgument(name = TRANSACTION_DETAIL_PARAM, builder = {
                    type = NavType.IntType
                    nullable = false
                })
            )
        ) {
            val viewModel = hiltViewModel<TransactionDetailViewModel>()
            TransactionDetailScreen(
                modifier = modifier,
                uiStateFlow = viewModel.uiState,
                uiEventFlow = viewModel.uiEvent,
                handleUiAction = viewModel::handleUiAction,
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}