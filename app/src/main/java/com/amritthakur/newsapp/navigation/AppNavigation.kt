package com.amritthakur.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amritthakur.newsapp.presentation.navigation.NavigationCoordinator
import com.amritthakur.newsapp.presentation.navigation.Screen
import com.amritthakur.newsapp.presentation.screen.CountriesScreen
import com.amritthakur.newsapp.presentation.screen.HomeScreen
import com.amritthakur.newsapp.presentation.screen.LanguagesScreen
import com.amritthakur.newsapp.presentation.screen.NewsScreen
import com.amritthakur.newsapp.presentation.screen.SearchScreen
import com.amritthakur.newsapp.presentation.screen.SourcesScreen
import com.amritthakur.newsapp.presentation.viewmodel.HomeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.amritthakur.newsapp.presentation.navigation.NavigationChannel
import com.amritthakur.newsapp.presentation.viewmodel.CountriesViewModel
import com.amritthakur.newsapp.presentation.viewmodel.LanguagesViewModel
import com.amritthakur.newsapp.presentation.viewmodel.NewsViewModel
import com.amritthakur.newsapp.presentation.viewmodel.SearchViewModel
import com.amritthakur.newsapp.presentation.viewmodel.SourcesViewModel

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NavigationEntryPoint {
    fun navigationChannel(): NavigationChannel
}

@Composable
fun AppNavigation(
    modifier: Modifier
) {
    val context = LocalContext.current

    val navController = rememberNavController()
    val navigationCoordinator = remember { NavigationCoordinator(navController) }

    // Get NavigationChannel from Hilt
    val navigationChannel = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            NavigationEntryPoint::class.java
        ).navigationChannel()
    }

    // Observe navigation events
    val navigationEvent by navigationChannel.navigationEvent.collectAsStateWithLifecycle()

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let { event ->
            navigationCoordinator.handleNavigationEvent(event)
            navigationChannel.clearEvent()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            val homeViewModel : HomeViewModel = hiltViewModel()
            HomeScreen(
                input = homeViewModel,
                output = homeViewModel
            )
        }

        composable(
            route = "news?source={source}&country={country}&language={language}",
            arguments = listOf(
                navArgument("source") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("country") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("language") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString("source")
            val country = backStackEntry.arguments?.getString("country")
            val language = backStackEntry.arguments?.getString("language")

            val newsViewModel : NewsViewModel = hiltViewModel()
            newsViewModel.updateParams(source, country, language)
            NewsScreen(
                input = newsViewModel,
                output = newsViewModel
            )
        }

        composable(Screen.Sources.route) {
            val sourcesViewModel : SourcesViewModel = hiltViewModel()
            SourcesScreen(
                input = sourcesViewModel,
                output = sourcesViewModel
            )
        }

        composable(Screen.Countries.route) {
            val countriesViewModel : CountriesViewModel = hiltViewModel()
            CountriesScreen(
                input = countriesViewModel,
                output = countriesViewModel
            )
        }

        composable(Screen.Languages.route) {
            val languagesViewModel : LanguagesViewModel = hiltViewModel()
            LanguagesScreen(
                input = languagesViewModel,
                output = languagesViewModel
            )
        }

        composable(Screen.Search.route) {
            val searchViewModel : SearchViewModel = hiltViewModel()
            SearchScreen(
                input = searchViewModel,
                output = searchViewModel
            )
        }
    }
}