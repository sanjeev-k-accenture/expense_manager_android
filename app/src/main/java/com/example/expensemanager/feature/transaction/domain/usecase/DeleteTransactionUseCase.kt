package com.example.expensemanager.feature.transaction.domain.usecase

import com.example.expensemanager.common.di.qualifier.IoDispatcher
import com.example.expensemanager.common.domain.UseCase
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: TransactionsRepository
) : UseCase<Int, Unit>(dispatcher) {

    override suspend fun execute(input: Int) {
        repository.deleteTransaction(id = input)
    }
}