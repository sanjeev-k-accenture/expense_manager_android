package com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.R
import com.example.expensemanager.feature.transaction.domain.usecase.DeleteTransactionUseCase
import com.example.expensemanager.feature.transaction.domain.usecase.GetTransactionDetailUseCase
import com.example.expensemanager.feature.transaction.presentation.navigation.TRANSACTION_DETAIL_PARAM
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.mapper.TransactionDetailUiMapper
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiAction
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiEvent
import com.example.expensemanager.feature.transaction.presentation.ui.transactiondetail.model.TransactionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val getTransactionDetailUseCase: GetTransactionDetailUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val transactionDetailUiMapper: TransactionDetailUiMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val transactionId: Int? = savedStateHandle[TRANSACTION_DETAIL_PARAM]

    private val _uiState =
        MutableStateFlow<TransactionDetailUiState>(TransactionDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<TransactionDetailUiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getTransactionDetail()
    }

    fun handleUiAction(action: TransactionDetailUiAction) {
        when (action) {
            TransactionDetailUiAction.DeleteTranaction -> {
                deleteTransaction()
            }
        }
    }

    private fun getTransactionDetail() {
        viewModelScope.launch {
            if (transactionId == null) {
                _uiState.update {
                    TransactionDetailUiState.Error(messageId = R.string.transaction_not_found)
                }
                return@launch
            }

            _uiState.update {
                TransactionDetailUiState.Loading
            }

            val ucResult = getTransactionDetailUseCase(input = transactionId)
            ucResult.fold(
                onSuccess = { transaction ->
                    val uiModel = transactionDetailUiMapper(input = transaction)
                    _uiState.update {
                        TransactionDetailUiState.Success(transaction = uiModel)
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        TransactionDetailUiState.Error()
                    }
                }
            )
        }
    }

    private fun deleteTransaction() {
        viewModelScope.launch {
            if (transactionId == null) return@launch

            _uiState.update {
                TransactionDetailUiState.Loading
            }

            val ucResult = deleteTransactionUseCase(input = transactionId)
            ucResult.fold(
                onSuccess = {
                    _uiEvent.tryEmit(TransactionDetailUiEvent.DeleteSuccess)
                },
                onFailure = {
                    _uiEvent.tryEmit(TransactionDetailUiEvent.DeleteFailure)
                }
            )
        }
    }
}