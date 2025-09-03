package com.amritthakur.newsapp.data.remote.util

import com.amritthakur.newsapp.data.remote.dto.ErrorDto
import com.amritthakur.newsapp.domain.common.Result.Error
import com.squareup.moshi.Moshi
import retrofit2.Response

fun Response<*>.toError(): Error {
    val backendError = this.errorBody()?.string().toErrorDto()
    return Error(
        httpCode = this.code(),
        errorCode = backendError?.code,
        errorMessage = backendError?.message
    )
}

fun Exception.toError(): Error {
    return Error(
        httpCode = -1, // Custom code for exceptions
        errorCode = this::class.simpleName,
        errorMessage = this.message
    )
}

private fun String?.toErrorDto(): ErrorDto? {
    return try {
        if (this.isNullOrBlank()) return null
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ErrorDto::class.java)
        adapter.fromJson(this)
    } catch (_: Exception) {
        null // If parsing fails, return null
    }
}
