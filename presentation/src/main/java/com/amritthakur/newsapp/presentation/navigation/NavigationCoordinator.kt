package com.amritthakur.newsapp.presentation.navigation

import androidx.navigation.NavController
import com.amritthakur.newsapp.presentation.viewmodel.CountriesNavigationEvent
import com.amritthakur.newsapp.presentation.viewmodel.HomeNavigationEvent
import com.amritthakur.newsapp.presentation.viewmodel.LanguagesNavigationEvent
import com.amritthakur.newsapp.presentation.viewmodel.SourcesNavigationEvent

class NavigationCoordinator(
    private val navController: NavController
) {

    fun handleNavigationEvent(navigationEvent: NavigationEvent) {
        when (navigationEvent) {
            is HomeNavigationEvent -> {
                handleHomeNavigationEvent(navigationEvent)
            }

            is SourcesNavigationEvent -> {
                handleSourcesNavigationEvent(navigationEvent)
            }

            is CountriesNavigationEvent -> {
                handleCountriesNavigationEvent(navigationEvent)
            }

            is LanguagesNavigationEvent -> {
                handleLanguagesNavigationEvent(navigationEvent)
            }
        }
    }

    private fun handleHomeNavigationEvent(homeNavigationEvent: HomeNavigationEvent) {
        when (homeNavigationEvent) {
            HomeNavigationEvent.NavigateToTopHeadlines -> {
                navController.navigate(
                    Screen.News.createRoute(
                        country = "us"
                    )
                )
            }

            HomeNavigationEvent.NavigateToNewsSources -> {
                navController.navigate(Screen.Sources.route)
            }

            HomeNavigationEvent.NavigateToCountries -> {
                navController.navigate(Screen.Countries.route)
            }

            HomeNavigationEvent.NavigateToLanguages -> {
                navController.navigate(Screen.Languages.route)
            }

            HomeNavigationEvent.NavigateToSearch -> {
                navController.navigate(Screen.Search.route)
            }
        }
    }

    private fun handleSourcesNavigationEvent(sourcesNavigationEvent: SourcesNavigationEvent) {
        when (sourcesNavigationEvent) {
            is SourcesNavigationEvent.NavigateToNews -> {
                navController.navigate(
                    Screen.News.createRoute(
                        source = sourcesNavigationEvent.sourceId
                    )
                )
            }
        }
    }

    private fun handleCountriesNavigationEvent(countriesNavigationEvent: CountriesNavigationEvent) {
        when (countriesNavigationEvent) {
            is CountriesNavigationEvent.NavigateToNews -> {
                navController.navigate(
                    Screen.News.createRoute(
                        country = countriesNavigationEvent.countryCode
                    )
                )
            }
        }
    }

    private fun handleLanguagesNavigationEvent(languagesNavigationEvent: LanguagesNavigationEvent) {
        when (languagesNavigationEvent) {
            is LanguagesNavigationEvent.NavigateToNews -> {
                navController.navigate(
                    Screen.News.createRoute(
                        language = languagesNavigationEvent.languageCode
                    )
                )
            }
        }
    }
}
