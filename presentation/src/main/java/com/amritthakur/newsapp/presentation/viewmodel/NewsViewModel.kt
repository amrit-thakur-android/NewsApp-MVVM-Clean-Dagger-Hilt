package com.amritthakur.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.domain.entity.NewsParams
import com.amritthakur.newsapp.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

interface NewsInput

interface NewsOutput {
    val uiState: StateFlow<PagingData<Article>>
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel(), NewsInput, NewsOutput {

    private val _uiState = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    override val uiState: StateFlow<PagingData<Article>> = _uiState

    private var currentParams = NewsParams()

    fun updateParams(source: String?, country: String?, language: String?) {
        currentParams = NewsParams(
            source = source,
            country = country,
            language = language
        )
        getNews()
    }

    private fun getNews() {
        getNewsUseCase(currentParams)
            .cachedIn(viewModelScope)
            .onEach {
                _uiState.value = it
            }.launchIn(viewModelScope)
    }
}
