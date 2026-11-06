package com.example.expensemanager.feature.transaction.domain.usecase

import com.example.expensemanager.TestTransactionsRepository
import com.example.expensemanager.testTransaction
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util.GetTransactionHomeUiDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionUseCasesTest {
    @Test
    fun addTransaction_passesInputToRepository() = runTest {
        val repository = TestTransactionsRepository()
        val input = TransactionFormInput(250, "Groceries", TransactionType.Debit)

        val result = AddTransactionUseCase(repository, Dispatchers.Unconfined)(input)

        assertTrue(result.isSuccess)
        assertEquals(input, repository.insertedTransaction)
    }

    @Test
    fun addTransaction_returnsRepositoryFailure() = runTest {
        val repository = TestTransactionsRepository().apply {
            insertTransactionFailure = IllegalStateException("insert failed")
        }

        val result = AddTransactionUseCase(repository, Dispatchers.Unconfined)(
            TransactionFormInput(250, "Groceries", TransactionType.Debit)
        )

        assertTrue(result.isFailure)
        assertEquals("insert failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun deleteTransaction_passesIdToRepository() = runTest {
        val repository = TestTransactionsRepository()

        val result = DeleteTransactionUseCase(Dispatchers.Unconfined, repository)(42)

        assertTrue(result.isSuccess)
        assertEquals(42, repository.deletedTransactionId)
    }

    @Test
    fun getTransactionDetail_returnsRequestedTransaction() = runTest {
        val transaction = testTransaction(id = 9)
        val repository = TestTransactionsRepository(transaction = transaction)

        val result = GetTransactionDetailUseCase(Dispatchers.Unconfined, repository)(9)

        assertEquals(transaction, result.getOrThrow())
        assertEquals(9, repository.requestedTransactionId)
    }

    @Test
    fun getAllTransactions_returnsTransactionsInReverseRepositoryOrder() = runTest {
        val transactions = listOf(testTransaction(id = 1), testTransaction(id = 2))
        val repository = TestTransactionsRepository(transactions = flowOf(transactions))

        val result = GetAllTransactionsUseCase(Dispatchers.Unconfined, repository)(Unit)
            .getOrThrow()
            .toList()

        assertEquals(listOf(2, 1), result.single().map { it.id })
    }

    @Test
    fun getTotalAmount_passesTransactionTypeToRepository() = runTest {
        val repository = TestTransactionsRepository(
            totalCredit = flowOf(750),
            totalDebit = flowOf(300),
        )

        val credit = GetTotalAmountCreditedOrDebitedUseCase(Dispatchers.Unconfined, repository)(
            TransactionType.Credit
        ).getOrThrow().toList()
        val debit = GetTotalAmountCreditedOrDebitedUseCase(Dispatchers.Unconfined, repository)(
            TransactionType.Debit
        ).getOrThrow().toList()

        assertEquals(listOf(750), credit)
        assertEquals(listOf(300), debit)
    }

    @Test
    fun getTransactionHomeUiData_combinesTotalsAndReversedTransactionOrder() = runTest {
        val repository = TestTransactionsRepository(
            transactions = flowOf(listOf(testTransaction(id = 1), testTransaction(id = 2))),
            totalCredit = flowOf(900),
            totalDebit = flowOf(400),
        )
        val getAll = GetAllTransactionsUseCase(Dispatchers.Unconfined, repository)
        val getTotal = GetTotalAmountCreditedOrDebitedUseCase(Dispatchers.Unconfined, repository)

        val result = GetTransactionHomeUiDataUseCase(
            Dispatchers.Unconfined,
            getTotal,
            getAll,
        )(Unit).getOrThrow().toList().single()

        assertEquals(400, result.totalDebit)
        assertEquals(900, result.totalCredit)
        assertEquals(listOf(1, 2), result.transactionHistoryList.map { it.id })
    }

    @Test
    fun getTransactionHomeUiData_usesZeroWhenTotalCannotBeRetrieved() = runTest {
        val repository = TestTransactionsRepository().apply {
            totalCreditFailure = IllegalStateException("credit unavailable")
            totalDebitFailure = IllegalStateException("debit unavailable")
        }
        val getAll = GetAllTransactionsUseCase(Dispatchers.Unconfined, repository)
        val getTotal = GetTotalAmountCreditedOrDebitedUseCase(Dispatchers.Unconfined, repository)

        val result = GetTransactionHomeUiDataUseCase(
            Dispatchers.Unconfined,
            getTotal,
            getAll,
        )(Unit).getOrThrow().toList().single()

        assertEquals(0, result.totalDebit)
        assertEquals(0, result.totalCredit)
    }

    @Test
    fun getTransactionHomeUiData_returnsFailureWhenTransactionsCannotBeRetrieved() = runTest {
        val repository = TestTransactionsRepository().apply {
            allTransactionsFailure = IllegalStateException("transactions unavailable")
        }
        val getAll = GetAllTransactionsUseCase(Dispatchers.Unconfined, repository)
        val getTotal = GetTotalAmountCreditedOrDebitedUseCase(Dispatchers.Unconfined, repository)

        val result = GetTransactionHomeUiDataUseCase(
            Dispatchers.Unconfined,
            getTotal,
            getAll,
        )(Unit)

        assertTrue(result.isFailure)
        assertEquals("transactions unavailable", result.exceptionOrNull()?.message)
    }
}
