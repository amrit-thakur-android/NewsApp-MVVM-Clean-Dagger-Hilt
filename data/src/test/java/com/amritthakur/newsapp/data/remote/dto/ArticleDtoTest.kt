package com.amritthakur.newsapp.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleDtoTest {

    @Test
    fun `toEntity should return empty list when input is null`() {
        // Given
        val articleDtos: List<ArticleDto>? = null

        // When
        val result = articleDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should return empty list when input is empty`() {
        // Given
        val articleDtos = emptyList<ArticleDto>()

        // When
        val result = articleDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should convert valid ArticleDto to Article`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "Breaking News Title",
                description = "This is a news description",
                url = "https://example.com/news",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertEquals(1, result.size)
        val article = result[0]
        assertEquals("bbc-news", article.sourceId)
        assertEquals("BBC News", article.sourceName)
        assertEquals("John Doe", article.author)
        assertEquals("Breaking News Title", article.title)
        assertEquals("This is a news description", article.description)
        assertEquals("https://example.com/news", article.url)
        assertEquals("https://example.com/image.jpg", article.urlToImage)
        assertEquals("2023-12-01T10:00:00Z", article.publishedAt)
        assertEquals("Full news content here", article.content)
    }

    @Test
    fun `toEntity should handle null source with empty defaults`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = null,
                author = "John Doe",
                title = "Breaking News Title",
                description = "This is a news description",
                url = "https://example.com/news",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertEquals(1, result.size)
        val article = result[0]
        assertEquals("", article.sourceId)
        assertEquals("", article.sourceName)
    }

    @Test
    fun `toEntity should handle source with null id and name`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = null, name = null),
                author = "John Doe",
                title = "Breaking News Title",
                description = "This is a news description",
                url = "https://example.com/news",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertEquals(1, result.size)
        val article = result[0]
        assertEquals("", article.sourceId)
        assertEquals("", article.sourceName)
    }

    @Test
    fun `toEntity should handle null optional fields with empty defaults`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = null,
                title = "Breaking News Title",
                description = null,
                url = "https://example.com/news",
                urlToImage = null,
                publishedAt = null,
                content = null
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertEquals(1, result.size)
        val article = result[0]
        assertEquals("", article.author)
        assertEquals("", article.description)
        assertEquals("", article.urlToImage)
        assertEquals("", article.publishedAt)
        assertEquals("", article.content)
    }

    @Test
    fun `toEntity should filter out articles with null title`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = null,
                description = "This is a news description",
                url = "https://example.com/news",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should filter out articles with blank title`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "   ",
                description = "This is a news description",
                url = "https://example.com/news",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should filter out articles with null url`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "Breaking News Title",
                description = "This is a news description",
                url = null,
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should filter out articles with blank url`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "Breaking News Title",
                description = "This is a news description",
                url = "",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should handle mixed valid and invalid articles`() {
        // Given
        val articleDtos = listOf(
            // Valid article
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "Valid News Title",
                description = "Valid description",
                url = "https://example.com/valid-news",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Valid content"
            ),
            // Invalid article - null title
            ArticleDto(
                source = ArticleSourceDto(id = "cnn", name = "CNN"),
                author = "Jane Doe",
                title = null,
                description = "Invalid description",
                url = "https://example.com/invalid-news",
                urlToImage = "https://example.com/image2.jpg",
                publishedAt = "2023-12-01T11:00:00Z",
                content = "Invalid content"
            ),
            // Valid article
            ArticleDto(
                source = ArticleSourceDto(id = "reuters", name = "Reuters"),
                author = "Bob Smith",
                title = "Another Valid Title",
                description = "Another valid description",
                url = "https://example.com/another-valid-news",
                urlToImage = "https://example.com/image3.jpg",
                publishedAt = "2023-12-01T12:00:00Z",
                content = "Another valid content"
            ),
            // Invalid article - blank url
            ArticleDto(
                source = ArticleSourceDto(id = "fox", name = "Fox News"),
                author = "Alice Johnson",
                title = "Invalid URL Article",
                description = "Invalid URL description",
                url = "   ",
                urlToImage = "https://example.com/image4.jpg",
                publishedAt = "2023-12-01T13:00:00Z",
                content = "Invalid URL content"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertEquals(2, result.size)
        assertEquals("Valid News Title", result[0].title)
        assertEquals("https://example.com/valid-news", result[0].url)
        assertEquals("Another Valid Title", result[1].title)
        assertEquals("https://example.com/another-valid-news", result[1].url)
    }

    @Test
    fun `toEntity should handle empty string title as invalid`() {
        // Given
        val articleDtos = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "",
                description = "This is a news description",
                url = "https://example.com/news",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here"
            )
        )

        // When
        val result = articleDtos.toEntity()

        // Then
        assertTrue(result.isEmpty()) // Empty string is considered blank, so should be filtered
    }
}
