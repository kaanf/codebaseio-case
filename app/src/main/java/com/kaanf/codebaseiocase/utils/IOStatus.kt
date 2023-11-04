package com.kaanf.codebaseiocase.utils

sealed class IOStatus<out T : Any> {
    data class Success<out T : Any>(val data: T) : IOStatus<T>()
    data class Failure(val exception: Exception) : IOStatus<Nothing>()
    data class Loading(val isLoading: Boolean) : IOStatus<Nothing>()
}