package com.amritthakur.newsapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amritthakur.newsapp.data.remote.datasource.NewsRemoteDataSource
import com.amritthakur.newsapp.data.remote.dto.ArticleDto
import com.amritthakur.newsapp.data.remote.dto.ArticleSourceDto
import com.amritthakur.newsapp.data.remote.response.NewsResponse
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.Article
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchNewsPagingSourceTest {

    private val mockNewsRemoteDataSource = mockk<NewsRemoteDataSource>()
    private val query = "android development"
    private val pagingSource = SearchNewsPagingSource(mockNewsRemoteDataSource, query)

    // ========== load() Success Tests ==========

    @Test
    fun `load should return first page with correct keys when loading page 1`() = runTest {
        // Given
        val articles = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "techcrunch", name = "TechCrunch"),
                author = "John Doe",
                title = "Android Development News",
                description = "Latest Android development trends",
                url = "https://techcrunch.com/android1",
                urlToImage = "https://techcrunch.com/image1.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Android development content"
            ),
            ArticleDto(
                source = ArticleSourceDto(id = "wired", name = "Wired"),
                author = "Jane Smith",
                title = "Mobile Development Guide",
                description = "Complete mobile development guide",
                url = "https://wired.com/mobile1",
                urlToImage = "https://wired.com/image1.jpg",
                publishedAt = "2023-12-01T11:00:00Z",
                content = "Mobile development content"
            )
        )
        val newsResponse = NewsResponse(totalResults = 150, articles = articles)
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 1
            )
        } returns Result.Success(newsResponse)

        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page

        assertEquals(2, pageResult.data.size)
        assertEquals("Android Development News", pageResult.data[0].title)
        assertEquals("https://techcrunch.com/android1", pageResult.data[0].url)
        assertEquals("Mobile Development Guide", pageResult.data[1].title)
        assertEquals("https://wired.com/mobile1", pageResult.data[1].url)

        assertNull(pageResult.prevKey) // First page has no previous key
        assertEquals(2, pageResult.nextKey) // Next page should be 2

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 1
            )
        }
    }

    @Test
    fun `load should return middle page with correct keys when loading page 3`() = runTest {
        // Given
        val articles = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "ars-technica", name = "Ars Technica"),
                author = "Tech Writer",
                title = "Advanced Android Features",
                description = "New Android features overview",
                url = "https://arstechnica.com/android3",
                urlToImage = "https://arstechnica.com/image3.jpg",
                publishedAt = "2023-12-01T12:00:00Z",
                content = "Advanced Android content"
            )
        )
        val newsResponse = NewsResponse(totalResults = 150, articles = articles)
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 3
            )
        } returns Result.Success(newsResponse)

        val loadParams =
            PagingSource.LoadParams.Append(key = 3, loadSize = 20, placeholdersEnabled = false)

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page

        assertEquals(1, pageResult.data.size)
        assertEquals("Advanced Android Features", pageResult.data[0].title)

        assertEquals(2, pageResult.prevKey) // Previous page should be 2
        assertEquals(4, pageResult.nextKey) // Next page should be 4

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 3
            )
        }
    }

    @Test
    fun `load should return last page with null nextKey when no more articles`() = runTest {
        // Given - Empty articles list indicates last page
        val newsResponse = NewsResponse(totalResults = 0, articles = emptyList())
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 5
            )
        } returns Result.Success(newsResponse)

        val loadParams =
            PagingSource.LoadParams.Append(key = 5, loadSize = 20, placeholdersEnabled = false)

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page

        assertTrue(pageResult.data.isEmpty())
        assertEquals(4, pageResult.prevKey) // Previous page should be 4
        assertNull(pageResult.nextKey) // No next page

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 5
            )
        }
    }

    @Test
    fun `load should remove duplicate articles by URL`() = runTest {
        // Given - Articles with duplicate URLs
        val articles = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "techcrunch", name = "TechCrunch"),
                author = "John Doe",
                title = "Android News 1",
                description = "First Android news",
                url = "https://example.com/android-news", // Same URL
                urlToImage = "https://example.com/image1.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "First content"
            ),
            ArticleDto(
                source = ArticleSourceDto(id = "wired", name = "Wired"),
                author = "Jane Smith",
                title = "Android News 2",
                description = "Second Android news",
                url = "https://example.com/android-news", // Duplicate URL
                urlToImage = "https://example.com/image2.jpg",
                publishedAt = "2023-12-01T11:00:00Z",
                content = "Second content"
            ),
            ArticleDto(
                source = ArticleSourceDto(id = "ars-technica", name = "Ars Technica"),
                author = "Tech Writer",
                title = "Different News",
                description = "Different news article",
                url = "https://example.com/different-news", // Unique URL
                urlToImage = "https://example.com/image3.jpg",
                publishedAt = "2023-12-01T12:00:00Z",
                content = "Different content"
            )
        )
        val newsResponse = NewsResponse(totalResults = 100, articles = articles)
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 1
            )
        } returns Result.Success(newsResponse)

        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page

        assertEquals(2, pageResult.data.size) // Only 2 unique articles should remain
        assertEquals("https://example.com/android-news", pageResult.data[0].url)
        assertEquals("https://example.com/different-news", pageResult.data[1].url)

        // First article with duplicate URL should be kept
        assertEquals("Android News 1", pageResult.data[0].title)
        assertEquals("Different News", pageResult.data[1].title)
    }

    @Test
    fun `load should handle null articles list`() = runTest {
        // Given
        val newsResponse = NewsResponse(totalResults = 0, articles = null)
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 1
            )
        } returns Result.Success(newsResponse)

        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page

        assertTrue(pageResult.data.isEmpty())
        assertNull(pageResult.prevKey)
        assertNull(pageResult.nextKey) // No next page when articles is null
    }

    // ========== load() Error Tests ==========

    @Test
    fun `load should return LoadResult Error when remote data source returns Result Error`() =
        runTest {
            // Given
            val error = Result.Error(
                httpCode = 429,
                errorCode = "rateLimited",
                errorMessage = "Rate limit exceeded"
            )
            coEvery {
                mockNewsRemoteDataSource.searchNews(
                    query = query,
                    pageSize = 20,
                    page = 1
                )
            } returns error

            val loadParams = PagingSource.LoadParams.Refresh<Int>(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )

            // When
            val result = pagingSource.load(loadParams)

            // Then
            assertTrue(result is PagingSource.LoadResult.Error)
            val errorResult = result as PagingSource.LoadResult.Error

            val exception = errorResult.throwable
            assertTrue(exception is Exception)
            assertEquals(
                "HttpCode: 429, ErrorCode: rateLimited, ErrorMessage: Rate limit exceeded",
                exception.message
            )
        }

    @Test
    fun `load should return LoadResult Error when remote data source throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Search service unavailable")
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = query,
                pageSize = 20,
                page = 1
            )
        } throws exception

        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error

        assertEquals(exception, errorResult.throwable)
    }

    // ========== Parameter Verification Tests ==========

    @Test
    fun `load should pass correct query and parameters to remote data source`() = runTest {
        // Given
        val customQuery = "kotlin programming"
        val customPagingSource = SearchNewsPagingSource(mockNewsRemoteDataSource, customQuery)

        val newsResponse = NewsResponse(totalResults = 0, articles = emptyList())
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = customQuery,
                pageSize = 20,
                page = 7
            )
        } returns Result.Success(newsResponse)

        val loadParams =
            PagingSource.LoadParams.Append(key = 7, loadSize = 20, placeholdersEnabled = false)

        // When
        val result = customPagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.searchNews(
                query = customQuery,
                pageSize = 20,
                page = 7
            )
        }
    }

    @Test
    fun `load should handle different query types correctly`() = runTest {
        // Given
        val specialQuery = "covid-19 & vaccine news!"
        val specialPagingSource = SearchNewsPagingSource(mockNewsRemoteDataSource, specialQuery)

        val newsResponse = NewsResponse(totalResults = 0, articles = emptyList())
        coEvery {
            mockNewsRemoteDataSource.searchNews(
                query = specialQuery,
                pageSize = 20,
                page = 1
            )
        } returns Result.Success(newsResponse)

        val loadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )

        // When
        val result = specialPagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.searchNews(
                query = specialQuery,
                pageSize = 20,
                page = 1
            )
        }
    }

    // ========== getRefreshKey Tests ==========

    @Test
    fun `getRefreshKey should return null when anchorPosition is null`() {
        // Given
        val state = mockk<PagingState<Int, Article>>()
        coEvery { state.anchorPosition } returns null

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertNull(refreshKey)
    }

    @Test
    fun `getRefreshKey should return correct key when anchorPosition is valid with prevKey`() {
        // Given
        val mockPage = mockk<PagingSource.LoadResult.Page<Int, Article>>()
        coEvery { mockPage.prevKey } returns 3
        coEvery { mockPage.nextKey } returns 5

        val state = mockk<PagingState<Int, Article>>()
        coEvery { state.anchorPosition } returns 15
        coEvery { state.closestPageToPosition(15) } returns mockPage

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertEquals(4, refreshKey) // prevKey + 1 = 3 + 1 = 4
    }

    @Test
    fun `getRefreshKey should return correct key when anchorPosition is valid with nextKey only`() {
        // Given
        val mockPage = mockk<PagingSource.LoadResult.Page<Int, Article>>()
        coEvery { mockPage.prevKey } returns null
        coEvery { mockPage.nextKey } returns 3

        val state = mockk<PagingState<Int, Article>>()
        coEvery { state.anchorPosition } returns 8
        coEvery { state.closestPageToPosition(8) } returns mockPage

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertEquals(2, refreshKey) // nextKey - 1 = 3 - 1 = 2
    }

    @Test
    fun `getRefreshKey should return null when closestPageToPosition returns null`() {
        // Given
        val state = mockk<PagingState<Int, Article>>()
        coEvery { state.anchorPosition } returns 12
        coEvery { state.closestPageToPosition(12) } returns null

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertNull(refreshKey)
    }
}