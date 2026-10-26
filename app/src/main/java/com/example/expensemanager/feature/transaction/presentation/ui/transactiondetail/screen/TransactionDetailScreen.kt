package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.expensemanager.R
import com.example.expensemanager.common.ui.ErrorView
import com.example.expensemanager.common.ui.LoadingView
import com.example.expensemanager.feature.transaction.presentation.navigation.TransactionDestination
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiEvent
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    uiStateFlow: StateFlow<TransactionDetailUiState>,
    handleUiAction: (TransactionDetailUiAction) -> Unit,
    uiEventFlow: SharedFlow<TransactionDetailUiEvent>,
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    val uiState = uiStateFlow.collectAsStateWithLifecycle()

    when (val currState = uiState.value) {
        is TransactionDetailUiState.Error -> {
            ErrorView(
                modifier = modifier,
                message = if (currState.messageId != null) stringResource(R.string.delete_transaction_failed) else null
            )
        }

        TransactionDetailUiState.Loading -> {
            LoadingView(modifier = modifier)
        }

        is TransactionDetailUiState.Success -> {
            TransactionDetailView(
                modifier = modifier,
                transaction = currState.transaction,
                handleUiAction = handleUiAction
            )
        }
    }

    val contextState = rememberUpdatedState(LocalContext.current)

    LaunchedEffect(uiEventFlow) {
        uiEventFlow.collect { event ->
            when (event) {
                TransactionDetailUiEvent.DeleteFailure -> {
                    snackbarHostState.showSnackbar(message = contextState.value.getString(R.string.delete_transaction_failed))
                }

                TransactionDetailUiEvent.DeleteSuccess -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        key = TransactionDestination.Detail.DELETE_SUCCESSFUL,
                        value = true
                    )
                    navController.popBackStack()
                }
            }
        }
    }
}