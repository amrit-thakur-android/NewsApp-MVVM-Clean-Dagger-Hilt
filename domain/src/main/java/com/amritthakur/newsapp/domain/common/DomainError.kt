package com.amritthakur.newsapp.domain.common

sealed class DomainError(open val message: String) {

    // Parameter errors
    data class ParameterInvalid(override val message: String) : DomainError(message)
    data class ParametersMissing(override val message: String) : DomainError(message)

    // Source related errors
    data class SourcesTooMany(override val message: String) : DomainError(message)
    data class SourceDoesNotExist(override val message: String) : DomainError(message)

    // API Key errors
    data class ApiKeyDisabled(override val message: String) : DomainError(message)
    data class ApiKeyExhausted(override val message: String) : DomainError(message)
    data class ApiKeyInvalid(override val message: String) : DomainError(message)
    data class ApiKeyMissing(override val message: String) : DomainError(message)

    // Rate limiting errors
    data class RateLimited(override val message: String) : DomainError(message)

    // Other errors
    data class UnexpectedError(override val message: String) : DomainError(message)
    data class NetworkError(override val message: String) : DomainError(message)
    data class ParsingError(override val message: String) : DomainError(message)
}