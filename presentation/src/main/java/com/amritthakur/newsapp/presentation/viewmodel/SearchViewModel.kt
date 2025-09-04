package com.amritthakur.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.domain.usecase.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

interface SearchInput {
    val onQueryChange: (String) -> Unit
}

interface SearchOutput {
    val query: StateFlow<String>
    val articles: StateFlow<PagingData<Article>>
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase
) : ViewModel(), SearchInput, SearchOutput {

    private val _query = MutableStateFlow("")
    override val query: StateFlow<String> = _query

    private val _articles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    override val articles: StateFlow<PagingData<Article>> = _articles

    private val _queryFlow = MutableStateFlow("")

    init {
        setupSearchFlow()
    }

    override val onQueryChange: (String) -> Unit = {
        _query.value = it
        _queryFlow.value = it
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun setupSearchFlow() {
        _queryFlow
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(PagingData.empty())
                } else {
                    searchNewsUseCase(query).cachedIn(viewModelScope)
                }
            }
            .onEach { pagingData ->
                _articles.value = pagingData
            }
            .launchIn(viewModelScope)
    }
}
