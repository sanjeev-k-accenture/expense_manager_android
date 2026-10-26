package com.example.expensemanager.common.database.transaction

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [TransactionEntity::class], version = 1)
@ColumnTypeConverters(TransactionTypeConverter::class)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DB_NAME = "expenses"
    }
}