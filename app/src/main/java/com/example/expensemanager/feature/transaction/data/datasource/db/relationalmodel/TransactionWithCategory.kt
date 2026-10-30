package com.example.expensemanager.feature.transaction.data.datasource.db.relationalmodel

import androidx.room3.Embedded
import androidx.room3.Relation
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.CategoryEntity
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.TransactionEntity

data class TransactionWithCategory(
    @Embedded
    val transaction: TransactionEntity,
    @Relation(
        parentColumns = ["categoryId"],
        entityColumns = ["id"]
    )
    val category: CategoryEntity
)
