package com.amritthakur.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.usecase.GetLanguagesUseCase
import com.amritthakur.newsapp.presentation.common.DispatcherProvider
import com.amritthakur.newsapp.presentation.navigation.NavigationChannel
import com.amritthakur.newsapp.presentation.navigation.NavigationEvent
import com.amritthakur.newsapp.presentation.state.LanguagesUiState
import com.amritthakur.newsapp.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface LanguagesInput {
    val onLanguage: (String) -> Unit
    val onTryAgain: () -> Unit
}

interface LanguagesOutput {
    val uiState: StateFlow<LanguagesUiState>
}

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val getLanguagesUseCase: GetLanguagesUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val navigationChannel: NavigationChannel
) : ViewModel(), LanguagesInput, LanguagesOutput {

    private val _uiState = MutableStateFlow(LanguagesUiState())
    override val uiState: StateFlow<LanguagesUiState> = _uiState

    init {
        getLanguages()
    }

    private fun getLanguages() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = _uiState.value.copy(
                languages = UiState.Loading
            )
            when (val outcome = getLanguagesUseCase()) {
                is Outcome.Success -> {
                    _uiState.value = _uiState.value.copy(
                        languages = UiState.Success(outcome.data)
                    )
                }

                is Outcome.Error -> {
                    _uiState.value = _uiState.value.copy(
                        languages = UiState.Error(outcome.error.message)
                    )
                }
            }
        }
    }

    override val onLanguage: (String) -> Unit = { languageCode ->
        navigationChannel.postEvent(LanguagesNavigationEvent.NavigateToNews(languageCode))
    }

    override val onTryAgain: () -> Unit = {
        getLanguages()
    }
}

sealed class LanguagesNavigationEvent : NavigationEvent {
    data class NavigateToNews(val languageCode: String) : LanguagesNavigationEvent()
}