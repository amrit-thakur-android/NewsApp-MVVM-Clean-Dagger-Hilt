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
import com.amritthakur.newsapp.domain.entity.Language
import com.amritthakur.newsapp.presentation.R
import com.amritthakur.newsapp.presentation.component.EmptyView
import com.amritthakur.newsapp.presentation.component.ErrorView
import com.amritthakur.newsapp.presentation.component.LoadingView
import com.amritthakur.newsapp.presentation.component.PrimaryButton
import com.amritthakur.newsapp.presentation.state.LanguagesUiState
import com.amritthakur.newsapp.presentation.state.UiState
import com.amritthakur.newsapp.presentation.viewmodel.LanguagesInput
import com.amritthakur.newsapp.presentation.viewmodel.LanguagesOutput

@Composable
fun LanguagesScreen(
    input: LanguagesInput,
    output: LanguagesOutput
) {

    LanguagesContent(
        uiState = output.uiState.collectAsStateWithLifecycle().value,
        onLanguage = input.onLanguage,
        onTryAgain = input.onTryAgain
    )
}

@Composable
fun LanguagesContent(
    uiState: LanguagesUiState,
    onLanguage: (String) -> Unit,
    onTryAgain: () -> Unit
) {
    when (uiState.languages) {
        is UiState.Loading -> {
            LoadingView()
        }

        is UiState.Success -> {
            val languages = uiState.languages.data

            if (languages.isEmpty()) {
                EmptyView(
                    message = stringResource(R.string.no_languages_found)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(32.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(
                        items = languages,
                        key = { language -> language.code }
                    ) { language ->
                        PrimaryButton(
                            title = language.name,
                            onClick = { onLanguage(language.code) }
                        )
                    }
                }
            }
        }

        is UiState.Error -> {
            ErrorView(
                message = uiState.languages.message,
                onTryAgain = onTryAgain
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguagesContentPreview() {
    val sampleLanguages = listOf(
        Language(code = "en", name = "English"),
        Language(code = "es", name = "Spanish"),
        Language(code = "fr", name = "French")
    )

    MaterialTheme {
        LanguagesContent(
            uiState = LanguagesUiState(languages = UiState.Success(sampleLanguages)),
            onLanguage = { },
            onTryAgain = { }
        )
    }
}