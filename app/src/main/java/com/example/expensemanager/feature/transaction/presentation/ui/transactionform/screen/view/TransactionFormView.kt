package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.screen.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormUiState

@Composable
fun TransactionFormView(
    modifier: Modifier = Modifier,
    uiState: TransactionFormUiState,
    onAmountChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onTransactionTypeChanged: (TransactionType) -> Unit,
    onSaveTransaction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(text = stringResource(R.string.new_transaction_title))

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.transactionFormUiModel.amount?.toString() ?: "",
            isError = uiState.isAmountError,
            label = {
                Text(stringResource(R.string.amount) + "*")
            },
            placeholder = {
                Text(
                    stringResource(R.string.amount_transacted),
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                )
            },
            suffix = {
                Text(
                    "EURO",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.padding(PaddingValues(start = 16.dp))
                )
            }, // TODO: Update currency - create a dropdown (maybe currency can be another feature)
            supportingText = if (uiState.isAmountError) {
                {
                    if (uiState.isAmountError) Text(
                        stringResource(
                            uiState.errorMessageId ?: R.string.enter_valid_amount
                        )
                    )
                }
            } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
            onValueChange = { value ->
                onAmountChanged(value)
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.transactionFormUiModel.description ?: "",
            isError = false,
            label = {
                Text(stringResource(R.string.transaction_description_title))
            },
            placeholder = {
                Text(
                    stringResource(R.string.transaction_description_subtitle),
                    style = TextStyle(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            onValueChange = { description ->
                onDescriptionChanged(description)
            }
        )

        TransactionTypeRadioButton(
            modifier = Modifier.padding(PaddingValues(vertical = 16.dp)),
            selectedTransactionType = uiState.transactionFormUiModel.type,
            onTransactionTypeSelected = { transactionType ->
                onTransactionTypeChanged(transactionType)
            }
        )

        // TODO: Add category dropdown

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(56.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            enabled = !uiState.isAmountError,
            onClick = onSaveTransaction
        ) {
            Text(stringResource(R.string.save_transaction))
        }
    }
}

@PreviewWithUi
@Composable
private fun TransactionFormViewPreview1() {
    TransactionFormView(
        modifier = Modifier,
        uiState = TransactionFormUiState(),
        onAmountChanged = {},
        onDescriptionChanged = {},
        onTransactionTypeChanged = {},
        onSaveTransaction = {}
    )
}

@PreviewWithUi
@Composable
private fun TransactionFormViewPreview2() {
    TransactionFormView(
        modifier = Modifier,
        uiState = TransactionFormUiState(
            isAmountError = true
        ),
        onAmountChanged = {},
        onDescriptionChanged = {},
        onTransactionTypeChanged = {},
        onSaveTransaction = {}
    )
}