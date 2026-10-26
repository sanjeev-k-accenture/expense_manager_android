package com.example.expensemanager.feature.transaction.data.repository

import com.example.expensemanager.feature.transaction.data.datasource.TransactionsLocalDataSource
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val localDataSource: TransactionsLocalDataSource
) : TransactionsRepository {
    override suspend fun getAllTransactions(): Flow<List<Transaction>> {
        return localDataSource.getAllTransactions()
    }

    override suspend fun getTransaction(transactionId: Int): Transaction {
        return localDataSource.getTransaction(transactionId = transactionId)

    }

    override suspend fun insertTransaction(transaction: TransactionFormInput) {
        localDataSource.insertTransaction(transaction = transaction)
    }

}