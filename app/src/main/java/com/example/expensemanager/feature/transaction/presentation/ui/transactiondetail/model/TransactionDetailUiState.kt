package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model

import androidx.annotation.StringRes

sealed interface TransactionDetailUiState {
    data object Loading : TransactionDetailUiState
    data class Success(val transaction: TransactionUiModel) : TransactionDetailUiState
    data class Error(@StringRes val messageId: Int? = null) : TransactionDetailUiState
}