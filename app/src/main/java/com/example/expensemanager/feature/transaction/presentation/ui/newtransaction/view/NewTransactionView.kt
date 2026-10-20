package com.example.expensemanager.feature.transaction.presentation.ui.newtransaction.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.common.model.di.qualifier.PreviewWithUi
import com.example.expensemanager.feature.transaction.presentation.ui.model.NewTransactionUiState

@Composable
fun NewTransactionView(modifier: Modifier = Modifier, uiState: NewTransactionUiState) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(text = stringResource(R.string.new_transaction_title))

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
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
            onValueChange = {},
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            isError = false,
            label = {
                Text(stringResource(R.string.transaction_description_title) + "*")
            },
            placeholder = {
                Text(
                    stringResource(R.string.transaction_description_subtitle),
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            onValueChange = {}
        )

        TransactionTypeRadioButton(
            modifier = Modifier.padding(PaddingValues(vertical = 16.dp)),
            onTransactionTypeSelected = {})

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(56.dp)
                .fillMaxWidth(),
            shape = RectangleShape,
            enabled = !uiState.isAmountError,
            onClick = {

            }) {
            Text(stringResource(R.string.save_transaction))
        }
    }
}

@PreviewWithUi
@Composable
private fun NewTransactionViewPreview1() {
    NewTransactionView(
        uiState = NewTransactionUiState()
    )
}

@PreviewWithUi
@Composable
private fun NewTransactionViewPreview2() {
    NewTransactionView(
        uiState = NewTransactionUiState(
            isAmountError = true
        )
    )
}