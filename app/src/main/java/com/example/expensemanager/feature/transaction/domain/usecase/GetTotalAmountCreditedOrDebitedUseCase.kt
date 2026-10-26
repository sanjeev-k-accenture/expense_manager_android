package com.example.expensemanager.feature.transaction.domain.usecase

import com.example.expensemanager.common.di.qualifier.IoDispatcher
import com.example.expensemanager.common.domain.UseCase
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalAmountCreditedOrDebitedUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val repository: TransactionsRepository
) : UseCase<TransactionType, Flow<Int>>(dispatcher) {
    override suspend fun execute(input: TransactionType): Flow<Int> {
        return repository.getTotalAmountCreditedOrDebited(transactionType = input)
    }
}