package com.example.expensemanager.feature.transaction.presentation.ui.model

import com.example.expensemanager.common.model.TransactionType

data class NewTransactionUiModel(
    val amount: Int,
    val description: String?,
    val type: TransactionType
)
