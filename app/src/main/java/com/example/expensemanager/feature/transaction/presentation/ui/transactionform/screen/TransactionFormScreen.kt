package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.expensemanager.R
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.presentation.navigation.TransactionDestination
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormEvent
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormUiModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormUiState
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.screen.view.TransactionFormView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TransactionFormScreen(
    modifier: Modifier = Modifier,
    uiStateFlow: StateFlow<TransactionFormUiState>,
    uiEventFlow: SharedFlow<TransactionFormEvent>,
    onFormUpdated: (TransactionFormUiModel) -> Unit,
    onSaveTransaction: () -> Unit,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    val uiState = uiStateFlow.collectAsStateWithLifecycle()

    val formData by lazy { uiState.value.transactionFormUiModel }

    TransactionFormView(
        modifier = modifier,
        uiState = uiState.value,
        onAmountChanged = { amount ->
            onFormUpdated(formData.copy(amount = amount.toIntOrNull()))
        },
        onDescriptionChanged = { description ->
            onFormUpdated(formData.copy(description = description))
        },
        onTransactionTypeChanged = { transactionType ->
            onFormUpdated(formData.copy(type = transactionType))
        },
        onSaveTransaction = onSaveTransaction
    )

    val contextState = rememberUpdatedState(LocalContext.current)
    val lifecycleOwner = LocalLifecycleOwner.current

    val failureMessage by lazy { contextState.value.getString(R.string.save_transaction_failed) }

    LaunchedEffect(uiEventFlow) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEventFlow.collect { event ->
                when (event) {
                    is TransactionFormEvent.SaveFailure -> {
                        snackbarHostState.showSnackbar(message = failureMessage)
                    }

                    TransactionFormEvent.SaveSuccessful -> {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            key = TransactionDestination.Form.SAVE_SUCCESSFUL,
                            value = true
                        )
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

@PreviewWithUi
@Composable
private fun TransactionFormScreenPreview1() {
    TransactionFormScreen(
        modifier = Modifier,
        uiStateFlow = MutableStateFlow(TransactionFormUiState()),
        onFormUpdated = {},
        onSaveTransaction = {},
        uiEventFlow = MutableSharedFlow(),
        navController = NavController(LocalContext.current),
        snackbarHostState = SnackbarHostState()
    )
}

@PreviewWithUi
@Composable
private fun TransactionFormScreenPreview2() {
    TransactionFormScreen(
        modifier = Modifier,
        uiStateFlow = MutableStateFlow(TransactionFormUiState(isAmountError = true)),
        onFormUpdated = {},
        onSaveTransaction = {},
        uiEventFlow = MutableSharedFlow(),
        navController = NavController(LocalContext.current),
        snackbarHostState = SnackbarHostState()
    )
}

@PreviewWithUi
@Composable
private fun TransactionFormScreenPreview3() {
    TransactionFormScreen(
        modifier = Modifier,
        uiStateFlow = MutableStateFlow(
            TransactionFormUiState(
                transactionFormUiModel = TransactionFormUiModel(
                    amount = 80,
                    description = "Gaming",
                    type = TransactionType.Debit
                )
            )
        ),
        onFormUpdated = {},
        onSaveTransaction = {},
        uiEventFlow = MutableSharedFlow(),
        navController = NavController(LocalContext.current),
        snackbarHostState = SnackbarHostState()
    )
}
