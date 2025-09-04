package com.amritthakur.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.usecase.GetSourcesUseCase
import com.amritthakur.newsapp.presentation.common.DispatcherProvider
import com.amritthakur.newsapp.presentation.navigation.NavigationChannel
import com.amritthakur.newsapp.presentation.navigation.NavigationEvent
import com.amritthakur.newsapp.presentation.state.SourcesUiState
import com.amritthakur.newsapp.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SourcesInput {
    val onSource: (String) -> Unit
    val onTryAgain: () -> Unit
}

interface SourcesOutput {
    val uiState: StateFlow<SourcesUiState>
}

@HiltViewModel
class SourcesViewModel @Inject constructor(
    private val getSourcesUseCase: GetSourcesUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val navigationChannel: NavigationChannel
) : ViewModel(), SourcesInput, SourcesOutput {

    private val _uiState = MutableStateFlow(SourcesUiState())
    override val uiState: StateFlow<SourcesUiState> = _uiState

    init {
        getSources()
    }

    private fun getSources() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.value = _uiState.value.copy(
                sources = UiState.Loading
            )
            when (val outcome = getSourcesUseCase()) {
                is Outcome.Success -> {
                    _uiState.value = _uiState.value.copy(
                        sources = UiState.Success(outcome.data)
                    )
                }

                is Outcome.Error -> {
                    _uiState.value = _uiState.value.copy(
                        sources = UiState.Error(outcome.error.message)
                    )
                }
            }
        }
    }

    override val onSource: (String) -> Unit = { sourceId ->
        navigationChannel.postEvent(SourcesNavigationEvent.NavigateToNews(sourceId))
    }

    override val onTryAgain: () -> Unit = {
        getSources()
    }
}

sealed class SourcesNavigationEvent : NavigationEvent {
    data class NavigateToNews(val sourceId: String) : SourcesNavigationEvent()
}
