package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model

sealed interface TransactionHomeUiState {
    data object Loading : TransactionHomeUiState
    data class Success(val transactionsHomeUiModel: TransactionHomeUiModel) :
        TransactionHomeUiState

    data object Empty : TransactionHomeUiState

    data class Error(val message: String? = null) : TransactionHomeUiState
}