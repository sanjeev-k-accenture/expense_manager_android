package com.example.expensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.expensemanager.config.theme.ExpenseManagerTheme
import com.example.expensemanager.feature.transaction.presentation.navigation.TransactionNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            ExpenseManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
                    SnackbarHost(snackbarHostState)
                }) { innerPadding ->
                    TransactionNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}
