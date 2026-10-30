package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.screen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHistoryItemUiModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util.transactionHistoryListForPreview

@Composable
fun TransactionHistoryItemView(
    modifier: Modifier = Modifier,
    transaction: TransactionHistoryItemUiModel
) {
    val isCredit = transaction.transactionType == TransactionType.Credit

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isCredit) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.error.copy(
                    alpha = 0.3f
                )
            )
            .padding(horizontal = 24.dp)
    ) {
        Text(
            transaction.categoryName,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(top = 12.dp)
                .background(color = MaterialTheme.colorScheme.inversePrimary, shape = CircleShape)
                .padding(all = 6.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .clipToBounds()
                    .weight(1f)
            ) {
                Text(
                    text = if (isCredit) "+" else "-",
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(transaction.amount.toString() + " " + transaction.currency)
            }
            Text(transaction.formattedTime)
        }

        Text(
            text = transaction.description,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@PreviewWithUi
@Composable
private fun TransactionHistoryItemViewPreview1() {
    Scaffold {
        TransactionHistoryItemView(
            modifier = Modifier.padding(it),
            transaction = transactionHistoryListForPreview[2]
        )
    }
}

@PreviewWithUi
@Composable
private fun TransactionHistoryItemViewPreview2() {
    Scaffold {
        TransactionHistoryItemView(
            modifier = Modifier.padding(it),
            transaction = transactionHistoryListForPreview[2]
        )
    }
}