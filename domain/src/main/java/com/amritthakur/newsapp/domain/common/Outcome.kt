package com.amritthakur.newsapp.domain.common

sealed class Outcome<out T> {
    data class Success<T>(val data: T) : Outcome<T>()
    data class Error(val error: DomainError) : Outcome<Nothing>()
}
