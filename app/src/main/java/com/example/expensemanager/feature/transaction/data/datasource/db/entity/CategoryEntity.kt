package com.example.expensemanager.feature.transaction.data.datasource.db.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

const val CATEGORY_TABLE_NAME = "category"

@Entity(tableName = CATEGORY_TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)
