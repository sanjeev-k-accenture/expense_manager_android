package com.example.expensemanager.feature.transaction.data.datasource

import com.example.expensemanager.common.database.transaction.TransactionDao
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.data.mapper.toTransaction
import com.example.expensemanager.feature.transaction.data.mapper.toTransactionEntity
import com.example.expensemanager.feature.transaction.data.mapper.toTransactionsList
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TransactionsLocalDataSource @Inject constructor(
    private val transactionDao: TransactionDao
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().flatMapLatest { entityList ->
            flowOf(entityList.toTransactionsList())
        }
    }

    fun getTotalAmountCreditedOrDebited(transactionType: TransactionType): Flow<Int> {
        return transactionDao.getTotalAmountCreditedOrDebited(transactionType)
    }

    suspend fun getTransaction(transactionId: Int): Transaction {
        val transactionEntity = transactionDao.getTransaction(id = transactionId)
        return transactionEntity.toTransaction()
    }

    suspend fun insertTransaction(transaction: TransactionFormInput) {
        val entity = transaction.toTransactionEntity()
        transactionDao.upsertTransaction(transactionEntity = entity)
    }

    suspend fun deleteTransaction(transactionId: Int) {
        transactionDao.deleteTransaction(id = transactionId)
    }
}