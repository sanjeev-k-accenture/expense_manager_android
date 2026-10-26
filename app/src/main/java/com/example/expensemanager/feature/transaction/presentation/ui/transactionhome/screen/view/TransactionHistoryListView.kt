package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.screen.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanager.config.theme.ExpenseManagerTheme
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHistoryItemUiModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util.transactionHistoryListForPreview

@Composable
fun TransactionHistoryListView(
    modifier: Modifier = Modifier,
    transactions: List<TransactionHistoryItemUiModel>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(items = transactions, key = { transaction ->
            transaction.id
        }) { transaction ->
            TransactionHistoryItemView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                transaction = transaction,
            )
        }
    }
}

@Preview
@Composable
private fun TransactionHistoryListViewPreview() {
    ExpenseManagerTheme {
        TransactionHistoryListView(transactions = transactionHistoryListForPreview)
    }
}
