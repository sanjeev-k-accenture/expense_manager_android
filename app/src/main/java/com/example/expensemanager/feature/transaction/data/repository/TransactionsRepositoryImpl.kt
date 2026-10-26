package com.example.expensemanager.feature.transaction.data.repository

import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.data.datasource.TransactionsLocalDataSource
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val localDataSource: TransactionsLocalDataSource
) : TransactionsRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return localDataSource.getAllTransactions()
    }

    override fun getTotalAmountCreditedOrDebited(transactionType: TransactionType): Flow<Int> {
        return localDataSource.getTotalAmountCreditedOrDebited(transactionType)
    }

    override suspend fun getTransaction(transactionId: Int): Transaction {
        return localDataSource.getTransaction(transactionId = transactionId)

    }

    override suspend fun insertTransaction(transaction: TransactionFormInput) {
        localDataSource.insertTransaction(transaction = transaction)
    }

    override suspend fun deleteTransaction(id: Int) {
        localDataSource.deleteTransaction(transactionId = id)
    }

}