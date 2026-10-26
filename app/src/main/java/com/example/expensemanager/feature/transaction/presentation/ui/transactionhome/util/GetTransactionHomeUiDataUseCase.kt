package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util

import com.example.expensemanager.common.di.qualifier.IoDispatcher
import com.example.expensemanager.common.domain.UseCase
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.domain.usecase.GetAllTransactionsUseCase
import com.example.expensemanager.feature.transaction.domain.usecase.GetTotalAmountCreditedOrDebitedUseCase
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.mapper.toUiModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetTransactionHomeUiDataUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val getTotalAmountCreditedOrDebitedUseCase: GetTotalAmountCreditedOrDebitedUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
) : UseCase<Unit, Flow<TransactionHomeUiModel>>(dispatcher) {
    override suspend fun execute(input: Unit): Flow<TransactionHomeUiModel> {
        val allTransactionsFlow = getAllTransactionsUseCase(Unit).getOrElse { throw it }

        val totalDebitedFlow =
            getTotalAmountCreditedOrDebitedUseCase(input = TransactionType.Debit).getOrNull()
                ?: flowOf(0)
        val totalCreditedFlow =
            getTotalAmountCreditedOrDebitedUseCase(input = TransactionType.Credit).getOrNull()
                ?: flowOf(0)

        val resFlow = combine(
            allTransactionsFlow,
            totalCreditedFlow,
            totalDebitedFlow
        ) { allTransactions, totalCredited, totalDebited ->
            TransactionHomeUiModel(
                transactionHistoryList = allTransactions.asReversed().toUiModel(),
                totalDebit = totalDebited,
                totalCredit = totalCredited
            )
        }

        return resFlow
    }
}