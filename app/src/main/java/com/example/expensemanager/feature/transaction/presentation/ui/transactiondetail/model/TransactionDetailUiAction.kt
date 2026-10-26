package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model

sealed interface TransactionDetailUiAction {
    data object DeleteTranaction : TransactionDetailUiAction
}