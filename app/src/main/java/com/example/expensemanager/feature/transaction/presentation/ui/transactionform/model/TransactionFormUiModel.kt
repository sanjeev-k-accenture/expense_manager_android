package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model

import com.example.expensemanager.common.model.TransactionType

data class TransactionFormUiModel(
    val amount: Int? = null,
    val description: String? = null,
    val type: TransactionType = TransactionType.Debit
)
