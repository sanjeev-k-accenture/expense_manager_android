package com.example.expensemanager.feature.transaction.data.mapper

import com.example.expensemanager.feature.transaction.data.datasource.db.entity.TransactionEntity
import com.example.expensemanager.feature.transaction.data.datasource.db.relationalmodel.TransactionWithCategory
import com.example.expensemanager.feature.transaction.domain.model.Category
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput

fun TransactionWithCategory.toTransaction(): Transaction {
    return Transaction(
        id = transaction.id,
        amount = transaction.amount,
        description = transaction.description,
        type = transaction.type,
        timestamp = transaction.timestamp,
        category = Category(
            id = category.id,
            name = category.name
        )
    )
}

fun List<TransactionWithCategory>.toTransactionsList(): List<Transaction> {
    return this.map { it.toTransaction() }
}

fun TransactionFormInput.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        amount = amount,
        description = description,
        type = type,
        categoryId = categoryId
    )
}
