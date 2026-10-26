package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.screen.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.common.model.TransactionType
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.config.theme.ExpenseManagerTheme

@Composable
fun TransactionTypeRadioButton(
    modifier: Modifier = Modifier,
    selectedTransactionType: TransactionType? = null,
    onTransactionTypeSelected: (TransactionType) -> Unit
) {
    val radioOptions = TransactionType.entries
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            selectedTransactionType ?: radioOptions[0]
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(stringResource(R.string.select_transaction_type))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            radioOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .selectable(
                            selected = (option == selectedOption),
                            onClick = {
                                onOptionSelected(option)
                                onTransactionTypeSelected(option)
                            },
                            role = Role.RadioButton
                        )
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedOption == option),
                        onClick = null,
                    )
                    Text(
                        text = stringResource(option.localeId),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@PreviewWithUi
@Composable
private fun TransactionTypeRadioButtonPreview() {
    ExpenseManagerTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TransactionTypeRadioButton {}
        }
    }
}
