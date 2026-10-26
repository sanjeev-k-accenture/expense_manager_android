package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model

import androidx.annotation.StringRes

sealed interface TransactionFormEvent {
    data class SaveFailure(@StringRes val messageId: Int) : TransactionFormEvent
    data object SaveSuccessful : TransactionFormEvent
}