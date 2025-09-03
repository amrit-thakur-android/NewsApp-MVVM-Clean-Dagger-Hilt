package com.amritthakur.newsapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.amritthakur.newsapp.presentation.viewmodel.SearchInput
import com.amritthakur.newsapp.presentation.viewmodel.SearchOutput

@Composable
fun SearchScreen(
    input: SearchInput,
    output: SearchOutput
) {
    val context = LocalContext.current
    val errorMessage = stringResource(R.string.custom_tab_failed)
    val query = output.query.collectAsStateWithLifecycle().value
    val lazyPagingItems = output.articles.collectAsLazyPagingItems()

    SearchContent(
        query = query,
        lazyPagingItems = lazyPagingItems,
        onQueryChange = input.onQueryChange,
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
fun SearchContent(
    query: String,
    lazyPagingItems: LazyPagingItems<Article>,
    onQueryChange: (String) -> Unit,
    onNews: (String) -> Unit
) {
    val loadState = lazyPagingItems.loadState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = {
                Text(text = stringResource(R.string.search_hint))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            },
            singleLine = true
        )

        // Handle initial loading state
        if (loadState.refresh is LoadState.Loading && query.isNotBlank()) {
            LoadingView()
        }

        // Handle refresh error state
        else if (loadState.refresh is LoadState.Error) {
            ErrorView(
                message = (loadState.refresh as LoadState.Error).error.message
                    ?: stringResource(R.string.something_went_wrong),
                onTryAgain = { lazyPagingItems.retry() }
            )
        }

        // Handle empty query state
        else if (query.isBlank()) {
            EmptyView(
                message = stringResource(R.string.enter_query)
            )
        }

        // Handle no results found
        else if (lazyPagingItems.itemCount == 0 && loadState.refresh is LoadState.NotLoading) {
            EmptyView(
                message = stringResource(R.string.no_news_found),
                messageInfo = stringResource(R.string.no_news_found_info_search)
            )
        }

        // Show articles list
        else {
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
    }
}