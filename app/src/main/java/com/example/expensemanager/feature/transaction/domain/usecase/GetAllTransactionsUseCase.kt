package com.example.expensemanager.feature.transaction.domain.usecase

import com.example.expensemanager.common.di.qualifier.IoDispatcher
import com.example.expensemanager.common.domain.UseCase
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val repository: TransactionsRepository
) : UseCase<Unit, Flow<List<Transaction>>>(dispatcher) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun execute(input: Unit): Flow<List<Transaction>> {
        val transactionsFlow = repository.getAllTransactions()
        return transactionsFlow.mapLatest { transactions ->
            transactions.asReversed()
        }

    }
}