package com.example.expensemanager.feature.transaction.data.datasource.db.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.example.expensemanager.common.model.TransactionType

const val TRANSACTION_TABLE_NAME = "transactions"

@Entity(
    tableName = TRANSACTION_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("categoryId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: Int,
    val amount: Int,
    val description: String,
    val type: TransactionType,
    val timestamp: Long = System.currentTimeMillis(),
)
