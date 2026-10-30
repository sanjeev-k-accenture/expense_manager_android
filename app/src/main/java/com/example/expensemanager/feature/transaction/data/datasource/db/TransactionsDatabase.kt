package com.example.expensemanager.feature.transaction.data.datasource.db

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.expensemanager.feature.transaction.data.datasource.db.converter.TransactionTypeConverter
import com.example.expensemanager.feature.transaction.data.datasource.db.dao.TransactionDao
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.CategoryEntity
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.TransactionEntity

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 2)
@ColumnTypeConverters(TransactionTypeConverter::class)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DB_NAME = "expenses"
    }
}