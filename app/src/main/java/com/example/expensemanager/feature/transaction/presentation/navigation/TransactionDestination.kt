package com.example.expensemanager.feature.transaction.presentation.navigation

const val TRANSACTION_HOME_ROUTE = "transaction_home"
const val TRANSACTION_FORM_ROUTE = "transaction_form"
// TODO: add transaction detail route (along with bottomsheet)

sealed class TransactionDestination(val route: String) {
    data object Home : TransactionDestination(route = TRANSACTION_HOME_ROUTE)
    data object Form : TransactionDestination(route = TRANSACTION_FORM_ROUTE) {
        const val SAVE_SUCCESSFUL = "SAVE_SUCCESSFUL"
    }
}