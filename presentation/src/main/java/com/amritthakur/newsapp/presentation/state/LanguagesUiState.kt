package com.amritthakur.newsapp.presentation.state

import com.amritthakur.newsapp.domain.entity.Language

data class LanguagesUiState(
    val languages: UiState<List<Language>> = UiState.Loading
)