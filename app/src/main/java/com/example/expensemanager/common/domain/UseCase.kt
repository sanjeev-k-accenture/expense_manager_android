package com.example.expensemanager.common.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<in T, out R>(private val dispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(input: T): Result<R> {
        return withContext(dispatcher) {
            try {
                val res = execute(input)
                Result.success(res)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    abstract suspend fun execute(input: T): R
}