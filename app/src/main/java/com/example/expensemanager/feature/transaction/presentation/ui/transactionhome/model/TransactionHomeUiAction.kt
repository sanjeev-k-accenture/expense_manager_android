package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model

sealed interface TransactionHomeUiAction {
    data object FetchTransactions: TransactionHomeUiAction
}