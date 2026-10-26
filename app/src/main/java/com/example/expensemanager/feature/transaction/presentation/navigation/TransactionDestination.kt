package com.example.expensemanager.feature.transaction.presentation.navigation

const val TRANSACTION_HOME_ROUTE = "transaction_home"
const val TRANSACTION_FORM_ROUTE = "transaction_form"
const val TRANSACTION_DETAIL_ROUTE = "transaction_detail"

const val TRANSACTION_DETAIL_PARAM = "transaction_id"

sealed class TransactionDestination(val route: String) {
    data object Home : TransactionDestination(route = TRANSACTION_HOME_ROUTE)
    data object Form : TransactionDestination(route = TRANSACTION_FORM_ROUTE) {
        const val SAVE_SUCCESSFUL = "SAVE_SUCCESSFUL"
    }

    data object Detail :
        TransactionDestination(route = "${TRANSACTION_DETAIL_ROUTE}/{${TRANSACTION_DETAIL_PARAM}}") {

        const val DELETE_SUCCESSFUL = "DELETE_SUCCESSFUL"

        fun createRoute(id: Int): String {
            return "${TRANSACTION_DETAIL_ROUTE}/$id"
        }
    }
}