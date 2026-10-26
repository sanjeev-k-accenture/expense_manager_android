package com.example.expensemanager.feature.transaction.data.di

import com.example.expensemanager.feature.transaction.data.repository.TransactionsRepositoryImpl
import com.example.expensemanager.feature.transaction.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface TransactionsDataModule {
    @Binds
    fun bindTransactionsRepository(impl: TransactionsRepositoryImpl): TransactionsRepository
}