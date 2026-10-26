package com.example.expensemanager.feature.transaction.presentation.ui.transactionform.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.R
import com.example.expensemanager.feature.transaction.domain.usecase.AddTransactionUseCase
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.mapper.TransactionFormUiDomainMapper
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormEvent
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormUiModel
import com.example.expensemanager.feature.transaction.presentation.ui.transactionform.model.TransactionFormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val mapper: TransactionFormUiDomainMapper,
) : ViewModel() {
    private val _formState = MutableStateFlow(TransactionFormUiState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<TransactionFormEvent>(extraBufferCapacity = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    fun updateForm(uiModel: TransactionFormUiModel) {
        val isAmountError = uiModel.amount != null && uiModel.amount <= 0
        _formState.update { state ->
            state.copy(transactionFormUiModel = uiModel, isAmountError = isAmountError)
        }
    }

    fun onSaveTransaction() {
        viewModelScope.launch {
            if (formState.value.isAmountError) return@launch

            val input = mapper(input = formState.value.transactionFormUiModel)

            val ucResult = addTransactionUseCase(input = input)

            when {
                ucResult.isFailure -> {
                    _eventFlow.emit(TransactionFormEvent.SaveFailure(messageId = R.string.save_transaction_failed))
                }

                ucResult.isSuccess -> {
                    _eventFlow.emit(TransactionFormEvent.SaveSuccessful)
                }
            }
        }
    }
}