package com.amritthakur.newsapp.domain.common

sealed class Result<out T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error(
        val httpCode: Int,
        val errorCode: String?,
        val errorMessage: String?
    ) : Result<Nothing>()
}
