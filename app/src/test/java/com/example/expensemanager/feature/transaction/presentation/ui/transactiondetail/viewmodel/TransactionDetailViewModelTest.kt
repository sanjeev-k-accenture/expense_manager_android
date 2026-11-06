package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.expensemanager.MainDispatcherRule
import com.example.expensemanager.TestTransactionsRepository
import com.example.expensemanager.testTransaction
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.usecase.DeleteTransactionUseCase
import com.example.expensemanager.feature.transaction.domain.usecase.GetTransactionDetailUseCase
import com.example.expensemanager.feature.transaction.presentation.navigation.TRANSACTION_DETAIL_PARAM
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.mapper.TransactionDetailUiMapper
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiEvent
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun initialization_loadsTransactionIntoSuccessState() = runTest(mainDispatcherRule.testDispatcher) {
        val transaction = testTransaction(id = 12, amount = 640, type = TransactionType.Credit)
        val repository = TestTransactionsRepository(transaction = transaction)
        val viewModel = createViewModel(repository, 12, mainDispatcherRule.testDispatcher)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is TransactionDetailUiState.Success)
        state as TransactionDetailUiState.Success
        assertEquals(12, state.transaction.id)
        assertEquals(640, state.transaction.amount)
        assertEquals(TransactionType.Credit, state.transaction.transactionType)
        assertEquals(12, repository.requestedTransactionId)
    }

    @Test
    fun initialization_reportsMissingTransactionId() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel(
            TestTransactionsRepository(),
            transactionId = null,
            dispatcher = mainDispatcherRule.testDispatcher,
        )

        advanceUntilIdle()

        assertEquals(
            TransactionDetailUiState.Error(com.example.expensemanager.R.string.transaction_not_found),
            viewModel.uiState.value,
        )
    }

    @Test
    fun initialization_reportsErrorWhenTransactionCannotBeLoaded() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository().apply {
            getTransactionFailure = IllegalStateException("load failed")
        }
        val viewModel = createViewModel(repository, 5, mainDispatcherRule.testDispatcher)

        advanceUntilIdle()

        assertEquals(TransactionDetailUiState.Error(), viewModel.uiState.value)
    }

    @Test
    fun deleteAction_emitsSuccessAfterDeletingTransaction() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository()
        val viewModel = createViewModel(repository, 21, mainDispatcherRule.testDispatcher)
        val event = backgroundScope.async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEvent.first()
        }

        viewModel.handleUiAction(TransactionDetailUiAction.DeleteTranaction)
        advanceUntilIdle()

        assertEquals(TransactionDetailUiEvent.DeleteSuccess, event.await())
        assertEquals(21, repository.deletedTransactionId)
    }

    @Test
    fun deleteAction_emitsFailureWhenRepositoryFails() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository().apply {
            deleteTransactionFailure = IllegalStateException("delete failed")
        }
        val viewModel = createViewModel(repository, 21, mainDispatcherRule.testDispatcher)
        val event = backgroundScope.async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiEvent.first()
        }

        viewModel.handleUiAction(TransactionDetailUiAction.DeleteTranaction)
        advanceUntilIdle()

        assertEquals(TransactionDetailUiEvent.DeleteFailure, event.await())
    }

    private fun createViewModel(
        repository: TestTransactionsRepository,
        transactionId: Int?,
        dispatcher: kotlinx.coroutines.test.TestDispatcher,
    ) = TransactionDetailViewModel(
        getTransactionDetailUseCase = GetTransactionDetailUseCase(dispatcher, repository),
        deleteTransactionUseCase = DeleteTransactionUseCase(dispatcher, repository),
        transactionDetailUiMapper = TransactionDetailUiMapper(dispatcher),
        savedStateHandle = SavedStateHandle(
            transactionId?.let { mapOf(TRANSACTION_DETAIL_PARAM to it) } ?: emptyMap()
        ),
    )
}
