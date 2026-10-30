package com.example.expensemanager.feature.transaction.data.datasource.db.migration

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.CATEGORY_TABLE_NAME
import com.example.expensemanager.feature.transaction.data.datasource.db.entity.TRANSACTION_TABLE_NAME

val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        // 1. Create category table
        connection.execSQL(
            "CREATE TABLE IF NOT EXISTS $CATEGORY_TABLE_NAME (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)"
        )

        // 2. Default category for existing transactions
        connection.execSQL(
            "INSERT INTO $CATEGORY_TABLE_NAME (id, name) VALUES (1, 'uncategorized')"
        )

        // 3. Create new transactions table with FK
        connection.execSQL(
            "CREATE TABLE ${TRANSACTION_TABLE_NAME}_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, categoryId INTEGER NOT NULL, amount INTEGER NOT NULL, description TEXT NOT NULL, type TEXT NOT NULL, timestamp INTEGER NOT NULL, FOREIGN KEY(categoryId) REFERENCES $CATEGORY_TABLE_NAME (id) ON UPDATE NO ACTION ON DELETE RESTRICT)"
        )

        // 4. Copy existing transactions table with default category id
        connection.execSQL(
            "INSERT INTO ${TRANSACTION_TABLE_NAME}_new (id, categoryId, amount, description, type, timestamp) SELECT id, 1, amount, description, type, timestamp FROM $TRANSACTION_TABLE_NAME"
        )

        // 5. Replace old transactions table
        connection.execSQL(
            "DROP TABLE $TRANSACTION_TABLE_NAME"
        )

        connection.execSQL(
            "ALTER TABLE ${TRANSACTION_TABLE_NAME}_new RENAME TO $TRANSACTION_TABLE_NAME"
        )

        // 6. Add FK Index
        connection.execSQL(
            "CREATE INDEX index_${TRANSACTION_TABLE_NAME}_categoryId ON $TRANSACTION_TABLE_NAME(categoryId)"
        )
    }
}