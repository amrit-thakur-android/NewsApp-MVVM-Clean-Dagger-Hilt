package com.amritthakur.newsapp.domain.usecase

import com.amritthakur.newsapp.domain.common.DomainError
import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.Language
import com.amritthakur.newsapp.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetLanguagesUseCaseTest {

    private val mockRepository = mockk<NewsRepository>()
    private val getLanguagesUseCase = GetLanguagesUseCase(mockRepository)

    @Test
    fun `invoke should return success when repository returns success with languages`() = runTest {
        // Given
        val languages = listOf(
            Language(code = "en", name = "English"),
            Language(code = "es", name = "Spanish"),
            Language(code = "fr", name = "French")
        )
        coEvery { mockRepository.getLanguages() } returns Result.Success(languages)

        // When
        val result = getLanguagesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Success)
        assertEquals(languages, (result as Outcome.Success).data)
        coVerify(exactly = 1) { mockRepository.getLanguages() }
    }

    @Test
    fun `invoke should return success with empty list when repository returns empty list`() =
        runTest {
            // Given
            val emptyLanguages = emptyList<Language>()
            coEvery { mockRepository.getLanguages() } returns Result.Success(emptyLanguages)

            // When
            val result = getLanguagesUseCase.invoke()

            // Then
            assertTrue(result is Outcome.Success)
            assertTrue((result as Outcome.Success).data.isEmpty())
            coVerify(exactly = 1) { mockRepository.getLanguages() }
        }

    @Test
    fun `invoke should return error when repository returns parameter invalid error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = 400,
            errorCode = "parameterInvalid",
            errorMessage = "Invalid parameter"
        )
        coEvery { mockRepository.getLanguages() } returns repositoryError

        // When
        val result = getLanguagesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.ParameterInvalid)
        assertEquals("Invalid parameter", error.message)
        coVerify(exactly = 1) { mockRepository.getLanguages() }
    }

    @Test
    fun `invoke should return error when repository returns network error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = -1,
            errorCode = "IOException",
            errorMessage = "No internet connection"
        )
        coEvery { mockRepository.getLanguages() } returns repositoryError

        // When
        val result = getLanguagesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.NetworkError)
        assertEquals(
            "Network error occurred",
            error.message
        ) // Uses default message for network errors
        coVerify(exactly = 1) { mockRepository.getLanguages() }
    }

    @Test
    fun `invoke should return error when repository returns local data error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = -1,
            errorCode = "LocalDataError",
            errorMessage = "Failed to load languages"
        )
        coEvery { mockRepository.getLanguages() } returns repositoryError

        // When
        val result = getLanguagesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.UnexpectedError) // LocalDataError maps to UnexpectedError in current implementation
        assertEquals("Failed to load languages", error.message)
        coVerify(exactly = 1) { mockRepository.getLanguages() }
    }

    @Test
    fun `invoke should return error when repository returns unexpected error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = 500,
            errorCode = "internalServerError",
            errorMessage = "Internal server error"
        )
        coEvery { mockRepository.getLanguages() } returns repositoryError

        // When
        val result = getLanguagesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.UnexpectedError)
        assertEquals("Internal server error", error.message)
        coVerify(exactly = 1) { mockRepository.getLanguages() }
    }

    @Test
    fun `invoke should handle null error message gracefully`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = 400,
            errorCode = "parameterInvalid",
            errorMessage = null
        )
        coEvery { mockRepository.getLanguages() } returns repositoryError

        // When
        val result = getLanguagesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.ParameterInvalid)
        // Should use default error message when null
        assertNotNull(error.message)
        assertTrue(error.message.isNotEmpty())
        coVerify(exactly = 1) { mockRepository.getLanguages() }
    }

    @Test
    fun `invoke should be called multiple times independently`() = runTest {
        // Given
        val languages = listOf(Language(code = "en", name = "English"))
        coEvery { mockRepository.getLanguages() } returns Result.Success(languages)

        // When
        val result1 = getLanguagesUseCase.invoke()
        val result2 = getLanguagesUseCase.invoke()

        // Then
        assertTrue(result1 is Outcome.Success)
        assertTrue(result2 is Outcome.Success)
        assertEquals((result1 as Outcome.Success).data, (result2 as Outcome.Success).data)
        coVerify(exactly = 2) { mockRepository.getLanguages() }
    }
}