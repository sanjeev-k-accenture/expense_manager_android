package com.example.expensemanager.feature.transaction.domain.repository

import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    suspend fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun getTransaction(transactionId: Int): Transaction
    suspend fun insertTransaction(transaction: TransactionFormInput)
}