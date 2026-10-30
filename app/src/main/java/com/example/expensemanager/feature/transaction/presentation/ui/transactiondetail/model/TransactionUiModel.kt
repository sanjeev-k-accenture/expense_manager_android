package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model

import com.example.expensemanager.common.model.TransactionType

data class TransactionUiModel(
    val id: Int,
    val title: String,
    val amount: Int,
    val time: String,
    val transactionType: TransactionType,
    val categoryName: String,
)
