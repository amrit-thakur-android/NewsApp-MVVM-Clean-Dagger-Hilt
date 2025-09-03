package com.amritthakur.newsapp.presentation.state

import com.amritthakur.newsapp.domain.entity.Country

data class CountriesUiState(
    val countries: UiState<List<Country>> = UiState.Loading
)