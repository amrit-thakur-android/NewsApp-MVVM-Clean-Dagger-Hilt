package com.amritthakur.newsapp.domain.common

fun Result.Error.toDomainError(): DomainError {
    return when (httpCode) {
        400 -> {
            when (errorCode) {
                "parameterInvalid" -> DomainError.ParameterInvalid(
                    errorMessage ?: ErrorMessage.PARAMETER_INVALID
                )

                "parametersMissing" -> DomainError.ParametersMissing(
                    errorMessage ?: ErrorMessage.PARAMETERS_MISSING
                )

                "sourcesTooMany" -> DomainError.SourcesTooMany(
                    errorMessage ?: ErrorMessage.SOURCES_TOO_MANY
                )

                "sourceDoesNotExist" -> DomainError.SourceDoesNotExist(
                    errorMessage ?: ErrorMessage.SOURCE_DOES_NOT_EXIST
                )

                else -> DomainError.UnexpectedError(errorMessage ?: ErrorMessage.UNEXPECTED_ERROR)
            }
        }

        401 -> {
            when (errorCode) {
                "apiKeyDisabled" -> DomainError.ApiKeyDisabled(
                    errorMessage ?: ErrorMessage.API_KEY_DISABLED
                )

                "apiKeyExhausted" -> DomainError.ApiKeyExhausted(
                    errorMessage ?: ErrorMessage.API_KEY_EXHAUSTED
                )

                "apiKeyInvalid" -> DomainError.ApiKeyInvalid(
                    errorMessage ?: ErrorMessage.API_KEY_INVALID
                )

                "apiKeyMissing" -> DomainError.ApiKeyMissing(
                    errorMessage ?: ErrorMessage.API_KEY_MISSING
                )

                else -> DomainError.UnexpectedError(errorMessage ?: ErrorMessage.UNEXPECTED_ERROR)
            }
        }

        429 -> {
            when (errorCode) {
                "rateLimited" -> DomainError.RateLimited(errorMessage ?: ErrorMessage.RATE_LIMITED)

                else -> DomainError.UnexpectedError(errorMessage ?: ErrorMessage.UNEXPECTED_ERROR)
            }
        }

        500 -> {
            DomainError.UnexpectedError(errorMessage ?: ErrorMessage.UNEXPECTED_ERROR)
        }

        -1 -> {
            when (errorCode) {
                "IOException",
                "SocketTimeoutException",
                "ConnectException",
                "UnknownHostException",
                "SocketException" -> DomainError.NetworkError(ErrorMessage.NETWORK_ERROR)

                else -> DomainError.UnexpectedError(errorMessage ?: ErrorMessage.UNEXPECTED_ERROR)
            }
        }

        -2 -> {
            DomainError.ParsingError(ErrorMessage.PARSING_ERROR)
        }

        else -> DomainError.UnexpectedError(errorMessage ?: ErrorMessage.UNEXPECTED_ERROR)
    }
}
