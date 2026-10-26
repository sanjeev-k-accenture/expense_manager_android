package com.example.expensemanager.feature.transaction.domain.repository

import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTotalAmountCreditedOrDebited(transactionType: TransactionType): Flow<Int>
    suspend fun getTransaction(transactionId: Int): Transaction
    suspend fun insertTransaction(transaction: TransactionFormInput)
    suspend fun deleteTransaction(id: Int)
}