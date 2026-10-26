package com.example.expensemanager.common.database.transaction

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.expensemanager.common.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM $TRANSACTION_TABLE_NAME")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM $TRANSACTION_TABLE_NAME WHERE type = :transactionType")
    fun getTotalAmountCreditedOrDebited(transactionType: TransactionType): Flow<Int>

    @Query("SELECT * FROM $TRANSACTION_TABLE_NAME WHERE id = :id")
    suspend fun getTransaction(id: Int): TransactionEntity

    @Upsert
    suspend fun upsertTransaction(transactionEntity: TransactionEntity)

    //    @Delete
    @Query("DELETE FROM $TRANSACTION_TABLE_NAME WHERE id = :id")
    suspend fun deleteTransaction(id: Int)
}