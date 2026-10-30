package com.example.expensemanager.common.di.module

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.expensemanager.feature.transaction.data.datasource.db.dao.TransactionDao
import com.example.expensemanager.feature.transaction.data.datasource.db.TransactionsDatabase
import com.example.expensemanager.feature.transaction.data.datasource.db.migration.MIGRATION_1_2
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
            .addMigrations(MIGRATION_1_2)
            .build()

        return db
    }

    @Provides
    fun provideTransactionDao(db: TransactionsDatabase): TransactionDao {
        return db.transactionDao()
    }
}