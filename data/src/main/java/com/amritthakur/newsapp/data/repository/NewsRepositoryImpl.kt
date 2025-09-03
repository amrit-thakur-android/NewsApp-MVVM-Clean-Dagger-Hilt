package com.amritthakur.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amritthakur.newsapp.data.local.datasource.NewsLocalDataSource
import com.amritthakur.newsapp.data.remote.datasource.NewsRemoteDataSource
import com.amritthakur.newsapp.data.remote.dto.toEntity
import com.amritthakur.newsapp.data.remote.paging.NewsPagingSource
import com.amritthakur.newsapp.data.remote.paging.SearchNewsPagingSource
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.domain.entity.Country
import com.amritthakur.newsapp.domain.entity.Language
import com.amritthakur.newsapp.domain.entity.NewsParams
import com.amritthakur.newsapp.domain.entity.Source
import com.amritthakur.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
        private const val MAX_SIZE = 200
    }

    override fun getNews(newsParams: NewsParams): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    newsRemoteDataSource = newsRemoteDataSource,
                    newsParams = newsParams
                )
            }
        ).flow
    }

    override suspend fun getSources(): Result<List<Source>> {
        return when (val result = newsRemoteDataSource.getSources()) {
            is Result.Success -> {
                Result.Success(result.data.sources.toEntity())
            }

            is Result.Error -> result
        }
    }

    override fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    newsRemoteDataSource = newsRemoteDataSource,
                    query = query
                )
            }
        ).flow
    }

    override suspend fun getCountries(): Result<List<Country>> {
        return try {
            val countriesMap = newsLocalDataSource.getCountries()
            val countries = countriesMap.map { (code, name) ->
                Country(code = code, name = name)
            }
            Result.Success(countries)
        } catch (exception: Exception) {
            Result.Error(-1, "LocalDataError", "Failed to load countries: ${exception.message}")
        }
    }

    override suspend fun getLanguages(): Result<List<Language>> {
        return try {
            val languagesMap = newsLocalDataSource.getLanguages()
            val languages = languagesMap.map { (code, name) ->
                Language(code = code, name = name)
            }
            Result.Success(languages)
        } catch (exception: Exception) {
            Result.Error(-1, "LocalDataError", "Failed to load languages: ${exception.message}")
        }
    }
}
