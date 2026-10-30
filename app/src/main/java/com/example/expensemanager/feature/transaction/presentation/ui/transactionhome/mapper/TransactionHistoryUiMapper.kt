package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.mapper

import com.example.expensemanager.common.util.formatDate
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHistoryItemUiModel

fun List<Transaction>.toUiModel(): List<TransactionHistoryItemUiModel> {
    return this.map { it.toTransactionHistoryUiModel() }
}

fun Transaction.toTransactionHistoryUiModel(): TransactionHistoryItemUiModel {
    return TransactionHistoryItemUiModel(
        id = id,
        amount = amount,
        description = description,
        transactionType = type,
        formattedTime = formatDate(timeStamp = timestamp),
        categoryName = category.name
    )
}