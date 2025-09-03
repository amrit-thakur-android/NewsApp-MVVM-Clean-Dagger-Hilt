package com.amritthakur.newsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorDto(
    val status: String?,
    val code: String?,
    val message: String?
)