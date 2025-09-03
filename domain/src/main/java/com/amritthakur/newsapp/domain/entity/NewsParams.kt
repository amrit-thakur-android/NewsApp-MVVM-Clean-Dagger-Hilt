package com.amritthakur.newsapp.domain.entity

data class NewsParams(
    val source: String? = null,
    val country: String? = null,
    val language: String? = null
)
