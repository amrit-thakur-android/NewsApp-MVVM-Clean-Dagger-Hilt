package com.amritthakur.newsapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amritthakur.newsapp.domain.entity.Country
import com.amritthakur.newsapp.presentation.R
import com.amritthakur.newsapp.presentation.component.EmptyView
import com.amritthakur.newsapp.presentation.component.ErrorView
import com.amritthakur.newsapp.presentation.component.LoadingView
import com.amritthakur.newsapp.presentation.component.PrimaryButton
import com.amritthakur.newsapp.presentation.state.CountriesUiState
import com.amritthakur.newsapp.presentation.state.UiState
import com.amritthakur.newsapp.presentation.viewmodel.CountriesInput
import com.amritthakur.newsapp.presentation.viewmodel.CountriesOutput

@Composable
fun CountriesScreen(
    input: CountriesInput,
    output: CountriesOutput
) {

    CountriesContent(
        uiState = output.uiState.collectAsStateWithLifecycle().value,
        onCountry = input.onCountry,
        onTryAgain = input.onTryAgain
    )
}

@Composable
fun CountriesContent(
    uiState: CountriesUiState,
    onCountry: (String) -> Unit,
    onTryAgain: () -> Unit
) {
    when (uiState.countries) {
        is UiState.Loading -> {
            LoadingView()
        }

        is UiState.Success -> {
            val countries = uiState.countries.data

            if (countries.isEmpty()) {
                EmptyView(
                    message = stringResource(R.string.no_countries_found)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(
                        items = countries,
                        key = { country -> country.code }
                    ) { country ->
                        PrimaryButton(
                            title = country.name,
                            onClick = { onCountry(country.code) }
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            ErrorView(
                message = uiState.countries.message,
                onTryAgain = onTryAgain
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountriesContentPreview() {
    val sampleCountries = listOf(
        Country(code = "us", name = "United States"),
        Country(code = "gb", name = "United Kingdom"),
        Country(code = "ca", name = "Canada")
    )

    MaterialTheme {
        CountriesContent(
            uiState = CountriesUiState(countries = UiState.Success(sampleCountries)),
            onCountry = { },
            onTryAgain = { }
        )
    }
}
