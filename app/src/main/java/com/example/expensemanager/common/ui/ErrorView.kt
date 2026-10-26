package com.example.expensemanager.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensemanager.R
import com.example.expensemanager.common.di.qualifier.PreviewWithUi

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    message: String? = null,
    onTryAgainClicked: (() -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .clipToBounds()
                .align(Alignment.Center)
        ) {
            Text(
                text = message ?: stringResource(R.string.common_error_message),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (onTryAgainClicked != null) {
                Button(
                    onClick = onTryAgainClicked,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.try_again))
                }
            }
        }
    }
}

@PreviewWithUi
@Composable
private fun ErrorViewPreview() {
    ErrorView(onTryAgainClicked = {})
}