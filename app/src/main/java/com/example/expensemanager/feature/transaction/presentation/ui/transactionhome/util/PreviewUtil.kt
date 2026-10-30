package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util

import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHistoryItemUiModel

val transactionHistoryListForPreview = List(10) { index ->
    TransactionHistoryItemUiModel(
        id = index,
        amount = index * 20 + 5,
        description = "My ${index}th transaction",
        formattedTime = "$index Oct 2026, 14:03 PM",
        transactionType = if (index % 3 == 0) TransactionType.Debit else TransactionType.Credit,
        categoryName = "uncategorized"
    )
}