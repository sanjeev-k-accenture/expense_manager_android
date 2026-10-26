package com.example.expensemanager.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensemanager.common.di.qualifier.PreviewWithUi

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    message: String,
    buttonText: String? = null,
    onButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        if (buttonText != null) {
            Button(
                onClick = onButtonClick,
                shape = RectangleShape,
                contentPadding = PaddingValues(horizontal = 56.dp)
            ) {
                Text(buttonText)
            }
        }
    }
}

@PreviewWithUi
@Composable
private fun EmptyViewPreview() {
    EmptyView(message = "Nothing to show here.\nWant to add something?", buttonText = "Yes")
}
