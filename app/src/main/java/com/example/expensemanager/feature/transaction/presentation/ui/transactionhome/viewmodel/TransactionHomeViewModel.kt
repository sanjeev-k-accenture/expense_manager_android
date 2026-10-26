package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.feature.transaction.domain.usecase.GetAllTransactionsUseCase
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.mapper.toUiModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransactionHomeUiState>(TransactionHomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getTransactions()
    }

    fun handleUiAction(action: TransactionHomeUiAction) {
        when (action) {
            TransactionHomeUiAction.FetchTransactions -> getTransactions()
        }
    }

    fun getTransactions() {
        viewModelScope.launch {
            val transactionsResult = getAllTransactionsUseCase(Unit)

            transactionsResult.fold(
                onSuccess = { transactionsFlow ->
                    transactionsFlow.collectLatest { transactions ->
                        _uiState.update {
                            if (transactions.isEmpty()) {
                                TransactionHomeUiState.Empty
                            } else {
                                TransactionHomeUiState.Success(transactions.toUiModel())
                            }
                        }
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        TransactionHomeUiState.Error(throwable.message)
                    }
                })
        }
    }
}