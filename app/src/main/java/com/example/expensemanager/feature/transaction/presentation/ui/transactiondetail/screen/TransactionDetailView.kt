package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionUiModel

@Composable
fun TransactionDetailView(
    modifier: Modifier = Modifier,
    transaction: TransactionUiModel,
    handleUiAction: (TransactionDetailUiAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        val details = stringResource(
            id = if (transaction.transactionType == TransactionType.Credit) R.string.transaction_credited else R.string.transaction_debited,
            transaction.amount,
            transaction.time
        )
        Text(text = transaction.title, style = MaterialTheme.typography.headlineLarge)
        Text(text = details)
        Row(
            modifier = Modifier
                .clipToBounds()
                .background(color = MaterialTheme.colorScheme.inversePrimary, shape = CircleShape)
                .padding(6.dp)
        ) {
            Text(text = stringResource(R.string.category))
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = transaction.categoryName,
                color = MaterialTheme.colorScheme.onBackground,
                fontStyle = FontStyle.Italic
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.transaction_id),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(end = 2.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = transaction.id.toString(),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
        }
        Spacer(modifier = Modifier.height(56.dp))
        Button(
            onClick = {
                handleUiAction(TransactionDetailUiAction.DeleteTranaction)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = MaterialTheme.colorScheme.error), shape = RectangleShape
        ) {
            Text(text = stringResource(R.string.delete_transaction))
        }
    }
}

@PreviewWithUi
@Composable
private fun TransactionDetailViewPreview() {
    TransactionDetailView(
        transaction = TransactionUiModel(
            id = 123,
            title = "Gaming",
            amount = 749,
            time = "26 Oct 2026, 15:34 PM",
            transactionType = TransactionType.Credit,
            categoryName = "uncategorized"
        ), handleUiAction = {}
    )
}