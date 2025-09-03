package com.amritthakur.newsapp.domain.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DomainErrorUtilTest {

    // ========== 400 Bad Request Errors ==========

    @Test
    fun `toDomainError should map 400 parameterInvalid with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "parameterInvalid",
            errorMessage = "Custom invalid parameter message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ParameterInvalid)
        assertEquals("Custom invalid parameter message", domainError.message)
    }

    @Test
    fun `toDomainError should map 400 parameterInvalid with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "parameterInvalid",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ParameterInvalid)
        assertEquals(ErrorMessage.PARAMETER_INVALID, domainError.message)
    }

    @Test
    fun `toDomainError should map 400 parametersMissing with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "parametersMissing",
            errorMessage = "Custom missing parameters message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ParametersMissing)
        assertEquals("Custom missing parameters message", domainError.message)
    }

    @Test
    fun `toDomainError should map 400 parametersMissing with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "parametersMissing",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ParametersMissing)
        assertEquals(ErrorMessage.PARAMETERS_MISSING, domainError.message)
    }

    @Test
    fun `toDomainError should map 400 sourcesTooMany with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "sourcesTooMany",
            errorMessage = "Custom too many sources message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.SourcesTooMany)
        assertEquals("Custom too many sources message", domainError.message)
    }

    @Test
    fun `toDomainError should map 400 sourcesTooMany with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "sourcesTooMany",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.SourcesTooMany)
        assertEquals(ErrorMessage.SOURCES_TOO_MANY, domainError.message)
    }

    @Test
    fun `toDomainError should map 400 sourceDoesNotExist with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "sourceDoesNotExist",
            errorMessage = "Custom source not found message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.SourceDoesNotExist)
        assertEquals("Custom source not found message", domainError.message)
    }

    @Test
    fun `toDomainError should map 400 sourceDoesNotExist with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "sourceDoesNotExist",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.SourceDoesNotExist)
        assertEquals(ErrorMessage.SOURCE_DOES_NOT_EXIST, domainError.message)
    }

    @Test
    fun `toDomainError should map 400 unknown errorCode with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "unknownError",
            errorMessage = "Custom unknown 400 error message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals("Custom unknown 400 error message", domainError.message)
    }

    @Test
    fun `toDomainError should map 400 unknown errorCode with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 400,
            errorCode = "unknownError",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals(ErrorMessage.UNEXPECTED_ERROR, domainError.message)
    }

    // ========== 401 Authentication Errors ==========

    @Test
    fun `toDomainError should map 401 apiKeyDisabled with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyDisabled",
            errorMessage = "Custom API key disabled message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyDisabled)
        assertEquals("Custom API key disabled message", domainError.message)
    }

    @Test
    fun `toDomainError should map 401 apiKeyDisabled with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyDisabled",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyDisabled)
        assertEquals(ErrorMessage.API_KEY_DISABLED, domainError.message)
    }

    @Test
    fun `toDomainError should map 401 apiKeyExhausted with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyExhausted",
            errorMessage = "Custom API key exhausted message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyExhausted)
        assertEquals("Custom API key exhausted message", domainError.message)
    }

    @Test
    fun `toDomainError should map 401 apiKeyExhausted with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyExhausted",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyExhausted)
        assertEquals(ErrorMessage.API_KEY_EXHAUSTED, domainError.message)
    }

    @Test
    fun `toDomainError should map 401 apiKeyInvalid with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyInvalid",
            errorMessage = "Custom API key invalid message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyInvalid)
        assertEquals("Custom API key invalid message", domainError.message)
    }

    @Test
    fun `toDomainError should map 401 apiKeyInvalid with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyInvalid",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyInvalid)
        assertEquals(ErrorMessage.API_KEY_INVALID, domainError.message)
    }

    @Test
    fun `toDomainError should map 401 apiKeyMissing with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyMissing",
            errorMessage = "Custom API key missing message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyMissing)
        assertEquals("Custom API key missing message", domainError.message)
    }

    @Test
    fun `toDomainError should map 401 apiKeyMissing with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "apiKeyMissing",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ApiKeyMissing)
        assertEquals(ErrorMessage.API_KEY_MISSING, domainError.message)
    }

    @Test
    fun `toDomainError should map 401 unknown errorCode with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "unknownAuthError",
            errorMessage = "Custom unknown auth error message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals("Custom unknown auth error message", domainError.message)
    }

    @Test
    fun `toDomainError should map 401 unknown errorCode with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 401,
            errorCode = "unknownAuthError",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals(ErrorMessage.UNEXPECTED_ERROR, domainError.message)
    }

    // ========== 429 Rate Limiting Errors ==========

    @Test
    fun `toDomainError should map 429 rateLimited with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 429,
            errorCode = "rateLimited",
            errorMessage = "Custom rate limited message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.RateLimited)
        assertEquals("Custom rate limited message", domainError.message)
    }

    @Test
    fun `toDomainError should map 429 rateLimited with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 429,
            errorCode = "rateLimited",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.RateLimited)
        assertEquals(ErrorMessage.RATE_LIMITED, domainError.message)
    }

    @Test
    fun `toDomainError should map 429 unknown errorCode with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 429,
            errorCode = "unknownRateError",
            errorMessage = "Custom unknown rate error message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals("Custom unknown rate error message", domainError.message)
    }

    @Test
    fun `toDomainError should map 429 unknown errorCode with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 429,
            errorCode = "unknownRateError",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals(ErrorMessage.UNEXPECTED_ERROR, domainError.message)
    }

    // ========== 500 Server Errors ==========

    @Test
    fun `toDomainError should map 500 with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 500,
            errorCode = "internalServerError",
            errorMessage = "Custom server error message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals("Custom server error message", domainError.message)
    }

    @Test
    fun `toDomainError should map 500 with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 500,
            errorCode = "internalServerError",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals(ErrorMessage.UNEXPECTED_ERROR, domainError.message)
    }

    // ========== -1 Network Errors ==========

    @Test
    fun `toDomainError should map -1 IOException to NetworkError with default message`() {
        // Given
        val error = Result.Error(
            httpCode = -1,
            errorCode = "IOException",
            errorMessage = "Custom network message" // This will be ignored
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.NetworkError)
        assertEquals(ErrorMessage.NETWORK_ERROR, domainError.message)
    }

    @Test
    fun `toDomainError should map -1 SocketTimeoutException to NetworkError with default message`() {
        // Given
        val error = Result.Error(
            httpCode = -1,
            errorCode = "SocketTimeoutException",
            errorMessage = "Custom timeout message" // This will be ignored
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.NetworkError)
        assertEquals(ErrorMessage.NETWORK_ERROR, domainError.message)
    }

    @Test
    fun `toDomainError should map -1 ConnectException to NetworkError with default message`() {
        // Given
        val error = Result.Error(
            httpCode = -1,
            errorCode = "ConnectException",
            errorMessage = "Custom connect message" // This will be ignored
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.NetworkError)
        assertEquals(ErrorMessage.NETWORK_ERROR, domainError.message)
    }

    @Test
    fun `toDomainError should map -1 UnknownHostException to NetworkError with default message`() {
        // Given
        val error = Result.Error(
            httpCode = -1,
            errorCode = "UnknownHostException",
            errorMessage = "Custom host message" // This will be ignored
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.NetworkError)
        assertEquals(ErrorMessage.NETWORK_ERROR, domainError.message)
    }

    @Test
    fun `toDomainError should map -1 SocketException to NetworkError with default message`() {
        // Given
        val error = Result.Error(
            httpCode = -1,
            errorCode = "SocketException",
            errorMessage = "Custom socket message" // This will be ignored
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.NetworkError)
        assertEquals(ErrorMessage.NETWORK_ERROR, domainError.message)
    }

    @Test
    fun `toDomainError should map -1 unknown exception with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = -1,
            errorCode = "UnknownException",
            errorMessage = "Custom unknown exception message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals("Custom unknown exception message", domainError.message)
    }

    @Test
    fun `toDomainError should map -1 unknown exception with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = -1,
            errorCode = "UnknownException",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals(ErrorMessage.UNEXPECTED_ERROR, domainError.message)
    }

    // ========== -2 Parsing Errors ==========

    @Test
    fun `toDomainError should map -2 to ParsingError with default message`() {
        // Given
        val error = Result.Error(
            httpCode = -2,
            errorCode = "parsingError",
            errorMessage = "Custom parsing message" // This will be ignored
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.ParsingError)
        assertEquals(ErrorMessage.PARSING_ERROR, domainError.message)
    }

    // ========== Unknown Error Codes ==========

    @Test
    fun `toDomainError should map unknown error code with custom message`() {
        // Given
        val error = Result.Error(
            httpCode = 999,
            errorCode = "unknownError",
            errorMessage = "Custom unknown error message"
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals("Custom unknown error message", domainError.message)
    }

    @Test
    fun `toDomainError should map unknown error code with default message when null`() {
        // Given
        val error = Result.Error(
            httpCode = 999,
            errorCode = "unknownError",
            errorMessage = null
        )

        // When
        val domainError = error.toDomainError()

        // Then
        assertTrue(domainError is DomainError.UnexpectedError)
        assertEquals(ErrorMessage.UNEXPECTED_ERROR, domainError.message)
    }
}