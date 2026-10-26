package com.example.expensemanager.common.di.module

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.expensemanager.common.database.transaction.TransactionDao
import com.example.expensemanager.common.database.transaction.TransactionsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTransactionsDB(@ApplicationContext applicationContext: Context): TransactionsDatabase {
        val db = Room.databaseBuilder<TransactionsDatabase>(
            applicationContext,
            TransactionsDatabase.DB_NAME
        )
            .setDriver(AndroidSQLiteDriver())
            .build()

        return db
    }

    @Provides
    fun provideTransactionDao(db: TransactionsDatabase): TransactionDao {
        return db.transactionDao()
    }
}