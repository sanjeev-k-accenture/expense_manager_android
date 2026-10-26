package com.example.expensemanager.feature.transaction.domain.usecase

import com.example.expensemanager.common.di.qualifier.IoDispatcher
import com.example.expensemanager.common.domain.UseCase
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<TransactionFormInput, Unit>(dispatcher) {

    override suspend fun execute(input: TransactionFormInput) {
        repository.insertTransaction(transaction = input)
    }
}