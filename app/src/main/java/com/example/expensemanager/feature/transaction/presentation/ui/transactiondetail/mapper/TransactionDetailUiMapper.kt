package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.mapper

import com.example.expensemanager.common.di.qualifier.DefaultDispatcher
import com.example.expensemanager.common.util.formatDate
import com.example.expensemanager.feature.transaction.domain.model.Transaction
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionDetailUiMapper @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(input: Transaction): TransactionUiModel {
        return withContext(dispatcher) {
            with(input) {

                val formattedTime = formatDate(timeStamp = timestamp)

                TransactionUiModel(
                    id = input.id,
                    title = description,
                    amount = amount,
                    time = formattedTime,
                    transactionType = type,
                    categoryName = category.name
                )
            }
        }
    }
}