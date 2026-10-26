package com.example.expensemanager.feature.transaction.domain.usecase

import com.example.expensemanager.common.di.qualifier.IoDispatcher
import com.example.expensemanager.common.domain.UseCase
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetTransactionDetailUseCase @Inject constructor(
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val repository: TransactionsRepository
) : UseCase<Int, Transaction>(coroutineDispatcher) {

    override suspend fun execute(input: Int): Transaction {
        return repository.getTransaction(transactionId = input)
    }
}
