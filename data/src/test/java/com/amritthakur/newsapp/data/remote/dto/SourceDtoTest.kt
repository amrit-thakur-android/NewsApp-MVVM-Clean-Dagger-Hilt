package com.amritthakur.newsapp.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SourceDtoTest {

    @Test
    fun `toEntity should return empty list when input is null`() {
        // Given
        val sourceDtos: List<SourceDto>? = null

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should return empty list when input is empty`() {
        // Given
        val sourceDtos = emptyList<SourceDto>()

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should convert valid SourceDto to Source`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "bbc-news",
                name = "BBC News",
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertEquals(1, result.size)
        val source = result[0]
        assertEquals("bbc-news", source.id)
        assertEquals("BBC News", source.name)
        assertEquals(
            "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
            source.description
        )
        assertEquals("http://www.bbc.co.uk/news", source.url)
        assertEquals("general", source.category)
        assertEquals("en", source.language)
        assertEquals("gb", source.country)
    }

    @Test
    fun `toEntity should handle null optional fields with empty defaults`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "bbc-news",
                name = "BBC News",
                description = null,
                url = null,
                category = null,
                language = null,
                country = null
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertEquals(1, result.size)
        val source = result[0]
        assertEquals("bbc-news", source.id)
        assertEquals("BBC News", source.name)
        assertEquals("", source.description)
        assertEquals("", source.url)
        assertEquals("", source.category)
        assertEquals("", source.language)
        assertEquals("", source.country)
    }

    @Test
    fun `toEntity should filter out sources with null id`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = null,
                name = "BBC News",
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should filter out sources with blank id`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "   ",
                name = "BBC News",
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should filter out sources with empty string id as invalid`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "",
                name = "BBC News",
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty()) // Empty string is considered blank, so should be filtered
    }

    @Test
    fun `toEntity should filter out sources with null name`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "bbc-news",
                name = null,
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should filter out sources with blank name`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "bbc-news",
                name = "   ",
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should filter out sources with empty string name as invalid`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "bbc-news",
                name = "",
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty()) // Empty string is considered blank, so should be filtered
    }

    @Test
    fun `toEntity should handle mixed valid and invalid sources`() {
        // Given
        val sourceDtos = listOf(
            // Valid source
            SourceDto(
                id = "bbc-news",
                name = "BBC News",
                description = "BBC News description",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            ),
            // Invalid source - null id
            SourceDto(
                id = null,
                name = "CNN",
                description = "CNN description",
                url = "http://www.cnn.com",
                category = "general",
                language = "en",
                country = "us"
            ),
            // Valid source
            SourceDto(
                id = "reuters",
                name = "Reuters",
                description = "Reuters description",
                url = "http://www.reuters.com",
                category = "general",
                language = "en",
                country = "us"
            ),
            // Invalid source - blank name
            SourceDto(
                id = "fox-news",
                name = "   ",
                description = "Fox News description",
                url = "http://www.foxnews.com",
                category = "general",
                language = "en",
                country = "us"
            ),
            // Valid source with null optional fields
            SourceDto(
                id = "techcrunch",
                name = "TechCrunch",
                description = null,
                url = null,
                category = null,
                language = null,
                country = null
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertEquals(3, result.size)

        // First valid source
        assertEquals("bbc-news", result[0].id)
        assertEquals("BBC News", result[0].name)
        assertEquals("BBC News description", result[0].description)

        // Second valid source
        assertEquals("reuters", result[1].id)
        assertEquals("Reuters", result[1].name)
        assertEquals("Reuters description", result[1].description)

        // Third valid source with null fields converted to empty strings
        assertEquals("techcrunch", result[2].id)
        assertEquals("TechCrunch", result[2].name)
        assertEquals("", result[2].description)
        assertEquals("", result[2].url)
        assertEquals("", result[2].category)
        assertEquals("", result[2].language)
        assertEquals("", result[2].country)
    }

    @Test
    fun `toEntity should handle sources with both id and name null`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = null,
                name = null,
                description = "Some description",
                url = "http://example.com",
                category = "general",
                language = "en",
                country = "us"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toEntity should handle sources with both id and name blank`() {
        // Given
        val sourceDtos = listOf(
            SourceDto(
                id = "   ",
                name = "   ",
                description = "Some description",
                url = "http://example.com",
                category = "general",
                language = "en",
                country = "us"
            )
        )

        // When
        val result = sourceDtos.toEntity()

        // Then
        assertTrue(result.isEmpty())
    }
}