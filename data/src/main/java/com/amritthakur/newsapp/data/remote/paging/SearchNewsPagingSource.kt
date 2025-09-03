package com.amritthakur.newsapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amritthakur.newsapp.data.remote.datasource.NewsRemoteDataSource
import com.amritthakur.newsapp.data.remote.dto.toEntity
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.Article
import javax.inject.Inject

class SearchNewsPagingSource @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val query: String
) : PagingSource<Int, Article>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val pageSize = PAGE_SIZE
        val page = params.key ?: STARTING_PAGE_INDEX

        return try {
            val result = newsRemoteDataSource.searchNews(
                query = query,
                pageSize = pageSize,
                page = page
            )

            when (result) {
                is Result.Success -> {
                    val rawArticles = result.data.articles ?: emptyList()
                    val articles = rawArticles.toEntity()
                        .distinctBy { it.url } // Remove duplicates by URL
                    val totalResults = result.data.totalResults ?: 0

                    // Handle NewsAPI's unpredictable behavior: stop if no articles returned
                    val hasNextPage = rawArticles.isNotEmpty() && totalResults > 0

                    LoadResult.Page(
                        data = articles,
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = if (hasNextPage) page + 1 else null
                    )
                }

                is Result.Error -> {
                    LoadResult.Error(
                        Exception("HttpCode: ${result.httpCode}, ErrorCode: ${result.errorCode}, ErrorMessage: ${result.errorMessage}")
                    )
                }
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}