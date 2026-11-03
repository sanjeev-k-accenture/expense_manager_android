package com.example.expensemanager.feature.transaction.data.datasource.db.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.TRANSACTION_TABLE_NAME
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.TransactionEntity
import com.example.expensemanager.feature.transaction.data.datasource.db.relationalmodel.TransactionWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM $TRANSACTION_TABLE_NAME")
    fun getAllTransactions(): Flow<List<TransactionWithCategory>>

    @Query("SELECT SUM(amount) FROM $TRANSACTION_TABLE_NAME WHERE type = :transactionType")
    fun getTotalAmountCreditedOrDebited(transactionType: TransactionType): Flow<Int>

    @Transaction
    @Query("SELECT * FROM $TRANSACTION_TABLE_NAME WHERE id = :id")
    suspend fun getTransaction(id: Int): TransactionWithCategory

    @Upsert
    suspend fun upsertTransaction(transactionEntity: TransactionEntity)

    //    @Delete
    @Query("DELETE FROM $TRANSACTION_TABLE_NAME WHERE id = :id")
    suspend fun deleteTransaction(id: Int)
}