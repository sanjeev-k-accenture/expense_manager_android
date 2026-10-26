package com.example.expensemanager.common.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher()

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher()

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher()
