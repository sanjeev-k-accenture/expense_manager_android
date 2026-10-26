package com.example.expensemanager.common.database.transaction

import androidx.room3.ColumnTypeConverter
import com.example.expensemanager.common.model.TransactionType

object TransactionTypeConverter {
    @ColumnTypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @ColumnTypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}