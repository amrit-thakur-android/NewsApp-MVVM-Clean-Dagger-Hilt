package com.amritthakur.newsapp.presentation.screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.presentation.R
import com.amritthakur.newsapp.presentation.component.EmptyView
import com.amritthakur.newsapp.presentation.component.ErrorView
import com.amritthakur.newsapp.presentation.component.LoadingView
import com.amritthakur.newsapp.presentation.component.NewsItem
import com.amritthakur.newsapp.presentation.viewmodel.NewsInput
import com.amritthakur.newsapp.presentation.viewmodel.NewsOutput

@Composable
fun NewsScreen(
    input: NewsInput,
    output: NewsOutput
) {
    val context = LocalContext.current
    val errorMessage = stringResource(R.string.custom_tab_failed)
    val lazyPagingItems = output.uiState.collectAsLazyPagingItems()

    NewsContent(
        lazyPagingItems = lazyPagingItems,
        onNews = { url ->
            launchCustomTab(
                context,
                url,
                errorMessage
            )
        }
    )
}

@Composable
fun NewsContent(
    lazyPagingItems: LazyPagingItems<Article>,
    onNews: (String) -> Unit
) {
    val loadState = lazyPagingItems.loadState

    // Handle initial loading state
    if (loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount == 0) {
        LoadingView()
        return
    }

    // Handle initial error state
    if (loadState.refresh is LoadState.Error && lazyPagingItems.itemCount == 0) {
        ErrorView(
            message = (loadState.refresh as LoadState.Error).error.message
                ?: stringResource(R.string.something_went_wrong),
            onTryAgain = { lazyPagingItems.retry() }
        )
        return
    }

    // Handle empty state
    if (loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0) {
        EmptyView(
            message = stringResource(R.string.no_news_found)
        )
        return
    }

    // Show articles list
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { article -> article.url },
            contentType = lazyPagingItems.itemContentType { "Article" }
        ) { index ->
            val article = lazyPagingItems[index]
            if (article != null) {
                NewsItem(
                    article = article,
                    onNews = { onNews(article.url) }
                )
            }
        }

        // Handle append loading state
        if (loadState.append is LoadState.Loading) {
            item {
                LoadingView()
            }
        }

        // Handle append error state
        if (loadState.append is LoadState.Error) {
            item {
                ErrorView(
                    message = (loadState.append as LoadState.Error).error.message
                        ?: stringResource(R.string.something_went_wrong),
                    onTryAgain = { lazyPagingItems.retry() }
                )
            }
        }
    }
}

fun launchCustomTab(
    context: Context,
    url: String,
    errorMessage: String
) {
    try {
        if (url.isBlank()) {
            return
        }

        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .build()
            )
            .setShowTitle(true)
            .setStartAnimations(
                context,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .setExitAnimations(
                context,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .build()

        customTabsIntent.launchUrl(context, url.toUri())
    } catch (_: Exception) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                e.message ?: errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}