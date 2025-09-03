package com.amritthakur.newsapp.data.local.datasource

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NewsLocalDataSourceTest {

    private val newsLocalDataSource = NewsLocalDataSource()

    // ========== getCountries Tests ==========

    @Test
    fun `getCountries should return non-empty map`() {
        // When
        val countries = newsLocalDataSource.getCountries()

        // Then
        assertTrue("Countries map should not be empty", countries.isNotEmpty())
    }

    @Test
    fun `getCountries should contain expected sample countries`() {
        // When
        val countries = newsLocalDataSource.getCountries()

        // Then
        // Test some well-known countries
        assertEquals("United States", countries["us"])
        assertEquals("United Kingdom", countries["gb"])
        assertEquals("Canada", countries["ca"])
        assertEquals("Germany", countries["de"])
        assertEquals("France", countries["fr"])
        assertEquals("Japan", countries["jp"])
        assertEquals("Australia", countries["au"])
        assertEquals("India", countries["in"])
    }

    @Test
    fun `getCountries should have consistent data format`() {
        // When
        val countries = newsLocalDataSource.getCountries()

        // Then
        countries.forEach { (code, name) ->
            // Country codes should be 2 characters lowercase
            assertEquals("Country code should be 2 characters", 2, code.length)
            assertTrue("Country code should be lowercase", code == code.lowercase())

            // Country names should not be empty
            assertTrue("Country name should not be empty", name.isNotEmpty())
            assertTrue("Country name should not be blank", name.isNotBlank())
        }
    }

    @Test
    fun `getCountries should return consistent data across multiple calls`() {
        // When
        val countries1 = newsLocalDataSource.getCountries()
        val countries2 = newsLocalDataSource.getCountries()

        // Then
        assertEquals("Countries should be consistent across calls", countries1, countries2)
        assertEquals("Countries size should be consistent", countries1.size, countries2.size)
    }

    // ========== getLanguages Tests ==========

    @Test
    fun `getLanguages should return non-empty map`() {
        // When
        val languages = newsLocalDataSource.getLanguages()

        // Then
        assertTrue("Languages map should not be empty", languages.isNotEmpty())
    }

    @Test
    fun `getLanguages should contain expected sample languages`() {
        // When
        val languages = newsLocalDataSource.getLanguages()

        // Then
        // Test some well-known languages
        assertEquals("English", languages["en"])
        assertEquals("Spanish", languages["es"])
        assertEquals("French", languages["fr"])
        assertEquals("German", languages["de"])
        assertEquals("Chinese", languages["zh"])
        assertEquals("Japanese", languages["ja"])
        assertEquals("Arabic", languages["ar"])
        assertEquals("Russian", languages["ru"])
    }

    @Test
    fun `getLanguages should have consistent data format`() {
        // When
        val languages = newsLocalDataSource.getLanguages()

        // Then
        languages.forEach { (code, name) ->
            // Language codes should be 2 characters lowercase
            assertEquals("Language code should be 2 characters", 2, code.length)
            assertTrue("Language code should be lowercase", code == code.lowercase())

            // Language names should not be empty
            assertTrue("Language name should not be empty", name.isNotEmpty())
            assertTrue("Language name should not be blank", name.isNotBlank())
        }
    }

    @Test
    fun `getLanguages should return consistent data across multiple calls`() {
        // When
        val languages1 = newsLocalDataSource.getLanguages()
        val languages2 = newsLocalDataSource.getLanguages()

        // Then
        assertEquals("Languages should be consistent across calls", languages1, languages2)
        assertEquals("Languages size should be consistent", languages1.size, languages2.size)
    }

    // ========== Data Integrity Tests ==========

    @Test
    fun `getCountries should have reasonable number of entries`() {
        // When
        val countries = newsLocalDataSource.getCountries()

        // Then
        // Should have a reasonable number of countries (at least 100, but not too many)
        assertTrue("Should have at least 100 countries", countries.size >= 100)
        assertTrue("Should have less than 300 countries", countries.size < 300)
    }

    @Test
    fun `getLanguages should have reasonable number of entries`() {
        // When
        val languages = newsLocalDataSource.getLanguages()

        // Then
        // Should have a reasonable number of languages (at least 50, but not too many)
        assertTrue("Should have at least 50 languages", languages.size >= 50)
        assertTrue("Should have less than 200 languages", languages.size < 200)
    }

    @Test
    fun `countries and languages should not have duplicate codes`() {
        // When
        val countries = newsLocalDataSource.getCountries()
        val languages = newsLocalDataSource.getLanguages()

        // Then
        val countryKeys = countries.keys.toSet()
        val languageKeys = languages.keys.toSet()

        assertEquals("Country codes should be unique", countries.size, countryKeys.size)
        assertEquals("Language codes should be unique", languages.size, languageKeys.size)
    }

    @Test
    fun `countries and languages should not have duplicate names`() {
        // When
        val countries = newsLocalDataSource.getCountries()
        val languages = newsLocalDataSource.getLanguages()

        // Then
        val countryValues = countries.values.toSet()
        val languageValues = languages.values.toSet()

        assertEquals("Country names should be unique", countries.size, countryValues.size)
        assertEquals("Language names should be unique", languages.size, languageValues.size)
    }
}