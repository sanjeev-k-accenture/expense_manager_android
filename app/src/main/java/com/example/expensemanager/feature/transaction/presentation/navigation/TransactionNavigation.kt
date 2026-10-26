package com.example.expensemanager.feature.transaction.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensemanager.R
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
            val successMessage = stringResource(R.string.save_transaction_success)
            LaunchedEffect(formResult) {
                if (formResult) {
                    snackbarHostState.showSnackbar(message = successMessage)
                    resultHandle.set(key = TransactionDestination.Form.SAVE_SUCCESSFUL, null)
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
    }
}