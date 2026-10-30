package com.example.expensemanager.feature.transaction.data.datasource.db.converter

import androidx.room3.ColumnTypeConverter
import com.example.expensemanager.common.model.TransactionType

object TransactionTypeConverter {
    @ColumnTypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @ColumnTypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}