package com.amritthakur.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.usecase.GetCountriesUseCase
import com.amritthakur.newsapp.presentation.common.DispatcherProvider
import com.amritthakur.newsapp.presentation.navigation.NavigationChannel
import com.amritthakur.newsapp.presentation.navigation.NavigationEvent
import com.amritthakur.newsapp.presentation.state.CountriesUiState
import com.amritthakur.newsapp.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CountriesInput {
    val onCountry: (String) -> Unit
    val onTryAgain: () -> Unit
}

interface CountriesOutput {
    val uiState: StateFlow<CountriesUiState>
}

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val navigationChannel: NavigationChannel
) : ViewModel(), CountriesInput, CountriesOutput {

    private val _uiState = MutableStateFlow(CountriesUiState())
    override val uiState: StateFlow<CountriesUiState> = _uiState

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = _uiState.value.copy(
                countries = UiState.Loading
            )
            when (val outcome = getCountriesUseCase()) {
                is Outcome.Success -> {
                    _uiState.value = _uiState.value.copy(
                        countries = UiState.Success(outcome.data)
                    )
                }

                is Outcome.Error -> {
                    _uiState.value = _uiState.value.copy(
                        countries = UiState.Error(outcome.error.message)
                    )
                }
            }
        }
    }

    override val onCountry: (String) -> Unit = { countryCode ->
        navigationChannel.postEvent(CountriesNavigationEvent.NavigateToNews(countryCode))
    }

    override val onTryAgain: () -> Unit = {
        getCountries()
    }
}

sealed class CountriesNavigationEvent : NavigationEvent {
    data class NavigateToNews(val countryCode: String) : CountriesNavigationEvent()
}
