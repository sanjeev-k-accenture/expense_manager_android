package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.viewmodel

import com.example.expensemanager.MainDispatcherRule
import com.example.expensemanager.TestTransactionsRepository
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.usecase.AddTransactionUseCase
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.mapper.TransactionFormUiDomainMapper
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormEvent
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionFormViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun updateForm_marksOnlyNonPositiveAmountsAsInvalid() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel(TestTransactionsRepository(), mainDispatcherRule.testDispatcher)

        viewModel.updateForm(TransactionFormUiModel(amount = 0))
        assertTrue(viewModel.formState.value.isAmountError)

        viewModel.updateForm(TransactionFormUiModel(amount = -1))
        assertTrue(viewModel.formState.value.isAmountError)

        viewModel.updateForm(TransactionFormUiModel(amount = 1))
        assertFalse(viewModel.formState.value.isAmountError)
        assertEquals(1, viewModel.formState.value.transactionFormUiModel.amount)
    }

    @Test
    fun saveTransaction_doesNotSaveInvalidAmount() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository()
        val viewModel = createViewModel(repository, mainDispatcherRule.testDispatcher)
        viewModel.updateForm(TransactionFormUiModel(amount = 0))

        viewModel.onSaveTransaction()
        advanceUntilIdle()

        assertNull(repository.insertedTransaction)
    }

    @Test
    fun saveTransaction_emitsSuccessAfterSaving() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository()
        val viewModel = createViewModel(repository, mainDispatcherRule.testDispatcher)
        val event = backgroundScope.async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.first()
        }
        val form = TransactionFormUiModel(
            amount = 350,
            description = "Dinner",
            type = TransactionType.Credit,
        )
        viewModel.updateForm(form)

        viewModel.onSaveTransaction()
        advanceUntilIdle()

        assertEquals(TransactionFormEvent.SaveSuccessful, event.await())
        assertEquals(350, repository.insertedTransaction?.amount)
        assertEquals("Dinner", repository.insertedTransaction?.description)
        assertEquals(TransactionType.Credit, repository.insertedTransaction?.type)
    }

    @Test
    fun saveTransaction_emitsFailureWhenRepositoryFails() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = TestTransactionsRepository().apply {
            insertTransactionFailure = IllegalStateException("save failed")
        }
        val viewModel = createViewModel(repository, mainDispatcherRule.testDispatcher)
        val event = backgroundScope.async(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.first()
        }
        viewModel.updateForm(TransactionFormUiModel(amount = 350))

        viewModel.onSaveTransaction()
        advanceUntilIdle()

        assertEquals(
            TransactionFormEvent.SaveFailure(com.example.expensemanager.R.string.save_transaction_failed),
            event.await(),
        )
    }

    private fun createViewModel(
        repository: TestTransactionsRepository,
        dispatcher: kotlinx.coroutines.test.TestDispatcher,
    ) = TransactionFormViewModel(
        AddTransactionUseCase(repository, dispatcher),
        TransactionFormUiDomainMapper(dispatcher),
    )
}
