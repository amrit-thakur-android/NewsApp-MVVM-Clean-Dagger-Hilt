package com.amritthakur.newsapp.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleSourceDto(
    val id: String?,
    val name: String?
)