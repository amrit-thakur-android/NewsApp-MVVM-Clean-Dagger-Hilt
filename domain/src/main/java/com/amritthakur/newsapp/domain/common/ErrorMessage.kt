package com.amritthakur.newsapp.domain.common

object ErrorMessage {
    const val PARAMETER_INVALID = "Invalid parameter in request"
    const val PARAMETERS_MISSING = "Required parameters are missing"
    const val SOURCES_TOO_MANY = "Too many sources"
    const val SOURCE_DOES_NOT_EXIST = "Source does not exist"
    const val API_KEY_DISABLED = "API key disabled"
    const val API_KEY_EXHAUSTED = "API key exhausted"
    const val API_KEY_INVALID = "API key invalid"
    const val API_KEY_MISSING = "API key missing"
    const val RATE_LIMITED = "Rate limited"
    const val NETWORK_ERROR = "Network error occurred"
    const val PARSING_ERROR = "Parsing error occurred"
    const val UNEXPECTED_ERROR = "Unexpected error occurred"
}
