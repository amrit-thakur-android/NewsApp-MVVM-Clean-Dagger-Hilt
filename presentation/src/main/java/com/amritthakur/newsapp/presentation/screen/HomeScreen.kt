package com.amritthakur.newsapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amritthakur.newsapp.presentation.R
import com.amritthakur.newsapp.presentation.component.PrimaryButton
import com.amritthakur.newsapp.presentation.viewmodel.HomeInput
import com.amritthakur.newsapp.presentation.viewmodel.HomeOutput

data class HomeActions(
    val onTopHeadLines: () -> Unit,
    val onNewsSources: () -> Unit,
    val onCountries: () -> Unit,
    val onLanguages: () -> Unit,
    val onSearch: () -> Unit
)

@Composable
fun HomeScreen(
    input: HomeInput,
    output: HomeOutput
) {
    val actions = HomeActions(
        onTopHeadLines = input.onTopHeadLines,
        onNewsSources = input.onNewsSources,
        onCountries = input.onCountries,
        onLanguages = input.onLanguages,
        onSearch = input.onSearch
    )

    HomeContent(
        actions = actions
    )
}

@Composable
fun HomeContent(
    actions: HomeActions
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            title = stringResource(R.string.top_headlines),
            onClick = actions.onTopHeadLines
        )

        Spacer(modifier = Modifier.weight(0.25f))

        PrimaryButton(
            title = stringResource(R.string.news_sources),
            onClick = actions.onNewsSources
        )

        Spacer(modifier = Modifier.weight(0.25f))

        PrimaryButton(
            title = stringResource(R.string.countries),
            onClick = actions.onCountries
        )

        Spacer(modifier = Modifier.weight(0.25f))

        PrimaryButton(
            title = stringResource(R.string.languages),
            onClick = actions.onLanguages
        )

        Spacer(modifier = Modifier.weight(0.25f))

        PrimaryButton(
            title = stringResource(R.string.search),
            onClick = actions.onSearch
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    val mockActions = HomeActions(
        onTopHeadLines = {},
        onNewsSources = {},
        onCountries = {},
        onLanguages = {},
        onSearch = {}
    )

    MaterialTheme {
        HomeContent(
            actions = mockActions
        )
    }
}
