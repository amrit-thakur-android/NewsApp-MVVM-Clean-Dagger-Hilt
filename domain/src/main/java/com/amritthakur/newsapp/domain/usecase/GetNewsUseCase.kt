package com.amritthakur.newsapp.domain.usecase

import androidx.paging.PagingData
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.domain.entity.NewsParams
import com.amritthakur.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    operator fun invoke(newsParams: NewsParams = NewsParams()): Flow<PagingData<Article>> {
        return newsRepository.getNews(newsParams)
    }
}
