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
import com.amritthakur.newsapp.domain.entity.Source
import com.amritthakur.newsapp.presentation.R
import com.amritthakur.newsapp.presentation.component.EmptyView
import com.amritthakur.newsapp.presentation.component.ErrorView
import com.amritthakur.newsapp.presentation.component.LoadingView
import com.amritthakur.newsapp.presentation.component.PrimaryButton
import com.amritthakur.newsapp.presentation.state.SourcesUiState
import com.amritthakur.newsapp.presentation.state.UiState
import com.amritthakur.newsapp.presentation.viewmodel.SourcesInput
import com.amritthakur.newsapp.presentation.viewmodel.SourcesOutput

@Composable
fun SourcesScreen(
    input: SourcesInput,
    output: SourcesOutput
) {

    SourcesContent(
        uiState = output.uiState.collectAsStateWithLifecycle().value,
        onSource = input.onSource,
        onTryAgain = input.onTryAgain
    )
}

@Composable
fun SourcesContent(
    uiState: SourcesUiState,
    onSource: (String) -> Unit,
    onTryAgain: () -> Unit
) {
    when (uiState.sources) {
        is UiState.Loading -> {
            LoadingView()
        }

        is UiState.Success -> {
            val sources = uiState.sources.data

            if (sources.isEmpty()) {
                EmptyView(
                    message = stringResource(R.string.no_sources_found)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(
                        items = sources,
                        key = { source -> source.id }
                    ) { source ->
                        PrimaryButton(
                            title = source.name,
                            onClick = { onSource(source.id) }
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            ErrorView(
                message = uiState.sources.message,
                onTryAgain = onTryAgain
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SourcesContentPreview() {
    val sampleSources = listOf(
        Source(
            id = "bbc-news",
            name = "BBC News",
            description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
            url = "http://www.bbc.co.uk/news",
            category = "general",
            language = "en",
            country = "gb"
        ),
        Source(
            id = "cnn",
            name = "CNN",
            description = "View the latest news and breaking news today for U.S., world, weather, entertainment, politics and health.",
            url = "http://us.cnn.com",
            category = "general",
            language = "en",
            country = "us"
        ),
        Source(
            id = "techcrunch",
            name = "TechCrunch",
            description = "TechCrunch is a leading technology media property, dedicated to obsessively profiling startups.",
            url = "https://techcrunch.com",
            category = "technology",
            language = "en",
            country = "us"
        )
    )

    MaterialTheme {
        SourcesContent(
            uiState = SourcesUiState(sources = UiState.Success(sampleSources)),
            onSource = { },
            onTryAgain = { }
        )
    }
}