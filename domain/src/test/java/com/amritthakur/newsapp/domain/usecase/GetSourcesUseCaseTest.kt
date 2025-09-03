package com.amritthakur.newsapp.domain.usecase

import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.Source
import com.amritthakur.newsapp.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetSourcesUseCaseTest {

    private val mockRepository = mockk<NewsRepository>()
    private val getSourcesUseCase = GetSourcesUseCase(mockRepository)

    @Test
    fun `invoke should return success when repository returns success with sources`() = runTest {
        // Given
        val sources = listOf(
            Source(
                id = "bbc-news",
                name = "BBC News",
                description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
                url = "http://www.bbc.co.uk/news",
                category = "general",
                language = "en",
                country = "gb"
            ),
            Source(
                id = "cnn",
                name = "CNN",
                description = "View the latest news and breaking news today for U.S., world, weather, entertainment, politics and health.",
                url = "http://us.cnn.com",
                category = "general",
                language = "en",
                country = "us"
            )
        )
        coEvery { mockRepository.getSources() } returns Result.Success(sources)

        // When
        val result = getSourcesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Success)
        assertEquals(sources, (result as Outcome.Success).data)
        coVerify(exactly = 1) { mockRepository.getSources() }
    }

    @Test
    fun `invoke should return success when repository returns success with empty list`() = runTest {
        // Given
        val emptySources = emptyList<Source>()
        coEvery { mockRepository.getSources() } returns Result.Success(emptySources)

        // When
        val result = getSourcesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Success)
        assertEquals(emptySources, (result as Outcome.Success).data)
        coVerify(exactly = 1) { mockRepository.getSources() }
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyInvalid",
            errorMessage = "Invalid API key"
        )
        coEvery { mockRepository.getSources() } returns error

        // When
        val result = getSourcesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        coVerify(exactly = 1) { mockRepository.getSources() }
    }
}