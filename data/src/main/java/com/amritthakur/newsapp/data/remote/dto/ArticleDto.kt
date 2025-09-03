package com.amritthakur.newsapp.data.remote.dto

import com.amritthakur.newsapp.domain.entity.Article
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleDto(
    val source: ArticleSourceDto?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

fun List<ArticleDto>?.toEntity(): List<Article> {
    if (this == null) return emptyList()
    val articles = mutableListOf<Article>()
    this.forEach {
        if (!it.title.isNullOrBlank() && !it.url.isNullOrBlank()) {
            articles.add(
                Article(
                    sourceId = it.source?.id ?: "",
                    sourceName = it.source?.name ?: "",
                    author = it.author ?: "",
                    title = it.title,
                    description = it.description ?: "",
                    url = it.url,
                    urlToImage = it.urlToImage ?: "",
                    publishedAt = it.publishedAt ?: "",
                    content = it.content ?: ""
                )
            )
        }
    }
    return articles
}