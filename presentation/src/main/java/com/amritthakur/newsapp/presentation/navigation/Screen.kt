package com.amritthakur.newsapp.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object News : Screen("news") {
        fun createRoute(
            source: String? = null,
            country: String? = null,
            language: String? = null
        ): String {
            val params = mutableListOf<String>()
            source?.let { params.add("source=$it") }
            country?.let { params.add("country=$it") }
            language?.let { params.add("language=$it") }

            return if (params.isNotEmpty()) {
                "news?${params.joinToString("&")}"
            } else {
                "news"
            }
        }
    }

    object Sources : Screen("sources")
    object Countries : Screen("countries")
    object Languages : Screen("languages")
    object Search : Screen("search")
}
