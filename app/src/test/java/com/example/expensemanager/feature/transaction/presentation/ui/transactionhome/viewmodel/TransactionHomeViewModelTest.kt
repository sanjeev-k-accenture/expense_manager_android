package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.viewmodel

import com.example.expensemanager.MainDispatcherRule
import com.example.expensemanager.TestTransactionsRepository
import com.example.expensemanager.testTransaction
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.usecase.GetAllTransactionsUseCase
import com.example.expensemanager.feature.transaction.domain.usecase.GetTotalAmountCreditedOrDebitedUseCase
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiState
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util.GetTransactionHomeUiDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionHomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialization_emitsTransactionsAndTotals() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository(
            transactions = MutableStateFlow(listOf(testTransaction(id = 1), testTransaction(id = 2))),
            totalCredit = MutableStateFlow(800),
            totalDebit = MutableStateFlow(250),
        )
        val viewModel = createViewModel(repository, mainDispatcherRule.testDispatcher)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TransactionHomeUiState.Success)
        state as TransactionHomeUiState.Success
        assertEquals(250, state.transactionsHomeUiModel.totalDebit)
        assertEquals(800, state.transactionsHomeUiModel.totalCredit)
        assertEquals(listOf(1, 2), state.transactionsHomeUiModel.transactionHistoryList.map { it.id })
    }

    @Test
    fun initialization_emitsEmptyWhenThereAreNoTransactions() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel(TestTransactionsRepository(), mainDispatcherRule.testDispatcher)

        advanceUntilIdle()

        assertEquals(TransactionHomeUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun initialization_emitsErrorWhenTransactionsCannotBeLoaded() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository().apply {
            allTransactionsFailure = IllegalStateException("load failed")
        }
        val viewModel = createViewModel(repository, mainDispatcherRule.testDispatcher)

        advanceUntilIdle()

        assertEquals(TransactionHomeUiState.Error("load failed"), viewModel.uiState.value)
    }

    @Test
    fun fetchTransactions_actionRefreshesObservedFlow() = runTest(mainDispatcherRule.testDispatcher) {
        val transactions = MutableStateFlow(listOf(testTransaction(id = 1)))
        val repository = TestTransactionsRepository(transactions = transactions)
        val viewModel = createViewModel(repository, mainDispatcherRule.testDispatcher)
        advanceUntilIdle()

        transactions.value = listOf(testTransaction(id = 2, type = TransactionType.Credit))
        viewModel.handleUiAction(TransactionHomeUiAction.FetchTransactions)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TransactionHomeUiState.Success)
        state as TransactionHomeUiState.Success
        assertEquals(listOf(2), state.transactionsHomeUiModel.transactionHistoryList.map { it.id })
    }

    private fun createViewModel(
        repository: TestTransactionsRepository,
        dispatcher: kotlinx.coroutines.test.TestDispatcher,
    ): TransactionHomeViewModel {
        val getAll = GetAllTransactionsUseCase(dispatcher, repository)
        val getTotal = GetTotalAmountCreditedOrDebitedUseCase(dispatcher, repository)
        return TransactionHomeViewModel(
            GetTransactionHomeUiDataUseCase(dispatcher, getTotal, getAll)
        )
    }
}
