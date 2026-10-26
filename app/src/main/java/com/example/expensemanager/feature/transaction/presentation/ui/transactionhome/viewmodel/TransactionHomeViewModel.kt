package com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.model.TransactionHomeUiState
import com.example.expensemanager.feature.transaction.presentation.ui.transactionhome.util.GetTransactionHomeUiDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHomeViewModel @Inject constructor(
    private val getTransactionHomeUiDataUseCase: GetTransactionHomeUiDataUseCase,
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

    private fun getTransactions() {
        viewModelScope.launch {
            val transactionsHomeResult = getTransactionHomeUiDataUseCase(Unit)

            transactionsHomeResult.fold(
                onSuccess = { resultFlow ->
                    resultFlow.collectLatest { result ->
                        _uiState.update {
                            if (result.transactionHistoryList.isEmpty()) {
                                TransactionHomeUiState.Empty
                            } else {
                                TransactionHomeUiState.Success(
                                    transactionsHomeUiModel = result
                                )
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