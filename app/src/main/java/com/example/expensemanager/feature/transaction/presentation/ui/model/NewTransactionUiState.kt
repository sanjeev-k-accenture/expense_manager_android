package com.example.expensemanager.feature.transaction.presentation.ui.model

import androidx.annotation.StringRes

data class NewTransactionUiState(
    val isAmountError: Boolean = false,
    @StringRes val errorMessageId: Int? = null,
)
