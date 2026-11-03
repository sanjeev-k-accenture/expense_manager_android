package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.screen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.common.di.qualifier.PreviewWithUi
import com.example.expensemanager.feature.transaction.domain.model.Category

@Composable
fun TransactionCategoryDropDown(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(stringResource(R.string.select_category))
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.secondaryFixed, shape = CircleShape)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        expanded = !expanded
                    }
            ) {
                Text(
                    text = "Categories",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Image(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    alignment = Alignment.Center,
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(category.name)
                        },
                        onClick = {
                            onCategorySelected(category)
                        }
                    )
                }
            }
        }
    }
}

@PreviewWithUi
@Composable
private fun TransactionCategoryDropDownPreview() {
    TransactionCategoryDropDown(
        modifier = Modifier,
        categories = List(10) {
            Category(id = it, name = "${it}th category")
        }) {}
}