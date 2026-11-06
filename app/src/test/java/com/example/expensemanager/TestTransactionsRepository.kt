package com.example.expensemanager

import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class TestTransactionsRepository(
    var transactions: Flow<List<Transaction>> = flowOf(emptyList()),
    var totalCredit: Flow<Int> = flowOf(0),
    var totalDebit: Flow<Int> = flowOf(0),
    var transaction: Transaction = testTransaction(),
) : TransactionsRepository {
    var allTransactionsFailure: Exception? = null
    var totalCreditFailure: Exception? = null
    var totalDebitFailure: Exception? = null
    var getTransactionFailure: Exception? = null
    var insertTransactionFailure: Exception? = null
    var deleteTransactionFailure: Exception? = null

    var requestedTransactionId: Int? = null
    var insertedTransaction: TransactionFormInput? = null
    var deletedTransactionId: Int? = null

    override fun getAllTransactions(): Flow<List<Transaction>> {
        allTransactionsFailure?.let { throw it }
        return transactions
    }

    override fun getTotalAmountCreditedOrDebited(transactionType: TransactionType): Flow<Int> {
        return when (transactionType) {
            TransactionType.Credit -> {
                totalCreditFailure?.let { throw it }
                totalCredit
            }

            TransactionType.Debit -> {
                totalDebitFailure?.let { throw it }
                totalDebit
            }
        }
    }

    override suspend fun getTransaction(transactionId: Int): Transaction {
        requestedTransactionId = transactionId
        getTransactionFailure?.let { throw it }
        return transaction
    }

    override suspend fun insertTransaction(transaction: TransactionFormInput) {
        insertTransactionFailure?.let { throw it }
        insertedTransaction = transaction
    }

    override suspend fun deleteTransaction(id: Int) {
        deleteTransactionFailure?.let { throw it }
        deletedTransactionId = id
    }
}

internal fun testTransaction(
    id: Int = 1,
    amount: Int = 100,
    description: String = "Test transaction",
    type: TransactionType = TransactionType.Debit,
    timestamp: Long = 0L,
) = Transaction(
    id = id,
    amount = amount,
    description = description,
    type = type,
    timestamp = timestamp,
)
