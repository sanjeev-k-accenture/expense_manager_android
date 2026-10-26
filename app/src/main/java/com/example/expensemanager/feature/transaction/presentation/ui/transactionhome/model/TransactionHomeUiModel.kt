package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model

data class TransactionHomeUiModel(
    val totalDebit: Int,
    val totalCredit: Int,
    val transactionHistoryList: List<TransactionHistoryItemUiModel>
)
