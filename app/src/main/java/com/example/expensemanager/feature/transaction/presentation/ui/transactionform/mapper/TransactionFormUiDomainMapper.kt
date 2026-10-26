package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.mapper

import com.example.expensemanager.common.di.qualifier.DefaultDispatcher
import com.example.expensemanager.feature.transaction.domain.model.TransactionFormInput
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionFormUiDomainMapper @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(input: TransactionFormUiModel): TransactionFormInput {
        return withContext(dispatcher) {
            with(input) {
                TransactionFormInput(
                    amount = amount ?: 0,
                    description = description ?: "",
                    type = type
                )
            }
        }
    }
}