package com.example.expensemanager.feature.transaction.domain.model

import com.example.expensemanager.common.model.TransactionType
import java.sql.Timestamp

data class Transaction(
    val id: Int,
    val amount: Int,
    val description: String,
    val type: TransactionType,
    val timestamp: Long
)
