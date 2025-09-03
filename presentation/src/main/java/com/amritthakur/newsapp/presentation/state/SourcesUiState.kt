package com.amritthakur.newsapp.presentation.state

import com.amritthakur.newsapp.domain.entity.Source

data class SourcesUiState(
    val sources: UiState<List<Source>> = UiState.Loading
)