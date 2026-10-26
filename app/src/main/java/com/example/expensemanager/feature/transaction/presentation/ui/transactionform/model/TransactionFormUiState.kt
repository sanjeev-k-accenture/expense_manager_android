package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model

import androidx.annotation.StringRes

data class TransactionFormUiState(
    val transactionFormUiModel: TransactionFormUiModel = TransactionFormUiModel(),
    val isAmountError: Boolean = false,
    @StringRes val errorMessageId: Int? = null,
)
