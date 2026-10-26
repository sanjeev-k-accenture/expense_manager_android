package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model

sealed interface TransactionDetailUiEvent {
    data object DeleteSuccess : TransactionDetailUiEvent
    data object DeleteFailure : TransactionDetailUiEvent
}