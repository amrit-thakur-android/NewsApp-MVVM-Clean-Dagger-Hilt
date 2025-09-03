package com.amritthakur.newsapp.data.remote.dto

import com.amritthakur.newsapp.domain.entity.Source
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceDto(
    val id: String?,
    val name: String?,
    val description: String?,
    val url: String?,
    val category: String?,
    val language: String?,
    val country: String?
)

fun List<SourceDto>?.toEntity(): List<Source> {
    if (this == null) return emptyList()
    val sources = mutableListOf<Source>()
    this.forEach {
        if (!it.id.isNullOrBlank() && !it.name.isNullOrBlank()) {
            sources.add(
                Source(
                    id = it.id,
                    name = it.name,
                    description = it.description ?: "",
                    url = it.url ?: "",
                    category = it.category ?: "",
                    language = it.language ?: "",
                    country = it.country ?: ""
                )
            )
        }
    }
    return sources
}
