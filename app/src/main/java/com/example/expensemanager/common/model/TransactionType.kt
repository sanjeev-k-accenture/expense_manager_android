package com.example.expensemanager.common.model

import androidx.annotation.StringRes
import com.example.expensemanager.R

enum class TransactionType(@StringRes val localeId: Int) {
    Credit(localeId = R.string.transaction_type_credit), Debit(localeId = R.string.transaction_type_debit)
}