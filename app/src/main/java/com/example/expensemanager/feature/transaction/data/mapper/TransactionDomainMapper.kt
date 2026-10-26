package com.example.expensemanager.feature.transaction.data.mapper

import com.example.expensemanager.common.database.transaction.TransactionEntity
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput

fun List<TransactionEntity>.toTransactionsList(): List<Transaction> {
    return this.map { it.toTransaction() }
}

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = id ?: 0, // TODO: Check with first transaction, is it
        amount = amount,
        description = description,
        type = type,
        timestamp = timestamp

    )
}

fun TransactionFormInput.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        amount = amount,
        description = description,
        type = type
    )
}
