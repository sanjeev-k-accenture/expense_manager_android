package com.example.expensemanager.common.database.transaction

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.expensemanager.common.model.TransactionType

const val TRANSACTION_TABLE_NAME = "transactions"

@Entity(tableName = TRANSACTION_TABLE_NAME)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null, // TODO: make it = 0 after checking with null
    val amount: Int,
    val description: String,
    val type: TransactionType,
    val timestamp: Long = System.currentTimeMillis(),
)
