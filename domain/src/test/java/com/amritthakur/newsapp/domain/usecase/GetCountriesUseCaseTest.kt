package com.amritthakur.newsapp.domain.usecase

import com.amritthakur.newsapp.domain.common.DomainError
import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.Country
import com.amritthakur.newsapp.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCountriesUseCaseTest {

    private val mockRepository = mockk<NewsRepository>()
    private val getCountriesUseCase = GetCountriesUseCase(mockRepository)

    @Test
    fun `invoke should return success when repository returns success with countries`() = runTest {
        // Given
        val countries = listOf(
            Country(code = "us", name = "United States"),
            Country(code = "gb", name = "United Kingdom"),
            Country(code = "ca", name = "Canada")
        )
        coEvery { mockRepository.getCountries() } returns Result.Success(countries)

        // When
        val result = getCountriesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Success)
        assertEquals(countries, (result as Outcome.Success).data)
        coVerify(exactly = 1) { mockRepository.getCountries() }
    }

    @Test
    fun `invoke should return success with empty list when repository returns empty list`() =
        runTest {
            // Given
            val emptyCountries = emptyList<Country>()
            coEvery { mockRepository.getCountries() } returns Result.Success(emptyCountries)

            // When
            val result = getCountriesUseCase.invoke()

            // Then
            assertTrue(result is Outcome.Success)
            assertTrue((result as Outcome.Success).data.isEmpty())
            coVerify(exactly = 1) { mockRepository.getCountries() }
        }

    @Test
    fun `invoke should return error when repository returns parameter invalid error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = 400,
            errorCode = "parameterInvalid",
            errorMessage = "Invalid parameter"
        )
        coEvery { mockRepository.getCountries() } returns repositoryError

        // When
        val result = getCountriesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.ParameterInvalid)
        assertEquals("Invalid parameter", error.message)
        coVerify(exactly = 1) { mockRepository.getCountries() }
    }

    @Test
    fun `invoke should return error when repository returns network error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = -1,
            errorCode = "IOException",
            errorMessage = "No internet connection"
        )
        coEvery { mockRepository.getCountries() } returns repositoryError

        // When
        val result = getCountriesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.NetworkError)
        assertEquals(
            "Network error occurred",
            error.message
        ) // Uses default message for network errors
        coVerify(exactly = 1) { mockRepository.getCountries() }
    }

    @Test
    fun `invoke should return error when repository returns local data error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = -1,
            errorCode = "LocalDataError",
            errorMessage = "Failed to load countries"
        )
        coEvery { mockRepository.getCountries() } returns repositoryError

        // When
        val result = getCountriesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.UnexpectedError) // LocalDataError maps to UnexpectedError in current implementation
        assertEquals("Failed to load countries", error.message)
        coVerify(exactly = 1) { mockRepository.getCountries() }
    }

    @Test
    fun `invoke should return error when repository returns unexpected error`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = 500,
            errorCode = "internalServerError",
            errorMessage = "Internal server error"
        )
        coEvery { mockRepository.getCountries() } returns repositoryError

        // When
        val result = getCountriesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.UnexpectedError)
        assertEquals("Internal server error", error.message)
        coVerify(exactly = 1) { mockRepository.getCountries() }
    }

    @Test
    fun `invoke should handle null error message gracefully`() = runTest {
        // Given
        val repositoryError = Result.Error(
            httpCode = 400,
            errorCode = "parameterInvalid",
            errorMessage = null
        )
        coEvery { mockRepository.getCountries() } returns repositoryError

        // When
        val result = getCountriesUseCase.invoke()

        // Then
        assertTrue(result is Outcome.Error)
        val error = (result as Outcome.Error).error
        assertTrue(error is DomainError.ParameterInvalid)
        // Should use default error message when null
        assertNotNull(error.message)
        assertTrue(error.message.isNotEmpty())
        coVerify(exactly = 1) { mockRepository.getCountries() }
    }

    @Test
    fun `invoke should be called multiple times independently`() = runTest {
        // Given
        val countries = listOf(Country(code = "us", name = "United States"))
        coEvery { mockRepository.getCountries() } returns Result.Success(countries)

        // When
        val result1 = getCountriesUseCase.invoke()
        val result2 = getCountriesUseCase.invoke()

        // Then
        assertTrue(result1 is Outcome.Success)
        assertTrue(result2 is Outcome.Success)
        assertEquals((result1 as Outcome.Success).data, (result2 as Outcome.Success).data)
        coVerify(exactly = 2) { mockRepository.getCountries() }
    }
}
