package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.screen.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.common.ui.EmptyView
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHistoryItemUiModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util.transactionHistoryListForPreview

@Composable
fun TransactionHomeView(
    modifier: Modifier = Modifier,
    transactions: List<TransactionHistoryItemUiModel>,
    onAddTxnBtnClick: () -> Unit,
) {
    if (transactions.isEmpty()) {
        EmptyView(
            message = stringResource(R.string.no_items),
            buttonText = stringResource(R.string.add_transaction),
            onButtonClick = onAddTxnBtnClick
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = modifier.fillMaxSize()) {
                Text(
                    stringResource(R.string.your_transactions),
                    style = MaterialTheme.typography.headlineMedium,
                )

                TransactionHistoryListView(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f),
                    transactions = transactions,
                )

                Button(
                    onClick = onAddTxnBtnClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                    contentPadding = PaddingValues(
                        vertical = 16.dp
                    )
                ) {
                    Text(stringResource(R.string.add_transaction))
                }
            }
        }
    }
}

@PreviewWithUi
@Composable
private fun TransactionHomeViewPreview() {
    TransactionHomeView(
        modifier = Modifier,
        transactions = transactionHistoryListForPreview,
        onAddTxnBtnClick = {})
}