package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model

import com.example.expensemanager.common.model.TransactionType

data class TransactionHistoryItemUiModel(
    val id: Int,
    val amount: Int,
    val currency: String = "EUR",
    val description: String = "",
    val transactionType: TransactionType,
    val formattedTime: String,
)
