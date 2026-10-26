package com.example.expensemanager.feature.transaction.domain.model

import com.example.expensemanager.common.model.TransactionType

data class TransactionFormInput(
    val amount: Int,
    val description: String,
    val type: TransactionType
)
