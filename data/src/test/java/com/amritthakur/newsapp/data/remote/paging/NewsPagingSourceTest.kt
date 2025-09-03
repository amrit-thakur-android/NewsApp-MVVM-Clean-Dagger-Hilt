package com.amritthakur.newsapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.amritthakur.newsapp.data.remote.datasource.NewsRemoteDataSource
import com.amritthakur.newsapp.data.remote.dto.ArticleDto
import com.amritthakur.newsapp.data.remote.dto.ArticleSourceDto
import com.amritthakur.newsapp.data.remote.response.NewsResponse
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.domain.entity.NewsParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NewsPagingSourceTest {

    private val mockNewsRemoteDataSource = mockk<NewsRemoteDataSource>()
    private val newsParams = NewsParams(country = "us", source = "bbc-news", language = "en")
    private val pagingSource = NewsPagingSource(mockNewsRemoteDataSource, newsParams)

    // ========== load() Success Tests ==========

    @Test
    fun `load should return first page with correct keys when loading page 1`() = runTest {
        // Given
        val articles = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "Test Article 1",
                description = "Test Description 1",
                url = "https://example.com/article1",
                urlToImage = "https://example.com/image1.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Test Content 1"
            ),
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "Jane Doe",
                title = "Test Article 2",
                description = "Test Description 2",
                url = "https://example.com/article2",
                urlToImage = "https://example.com/image2.jpg",
                publishedAt = "2023-12-01T11:00:00Z",
                content = "Test Content 2"
            )
        )
        val newsResponse = NewsResponse(totalResults = 100, articles = articles)
        coEvery {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
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
        assertEquals("Test Article 1", pageResult.data[0].title)
        assertEquals("https://example.com/article1", pageResult.data[0].url)
        assertEquals("Test Article 2", pageResult.data[1].title)
        assertEquals("https://example.com/article2", pageResult.data[1].url)

        assertNull(pageResult.prevKey) // First page has no previous key
        assertEquals(2, pageResult.nextKey) // Next page should be 2

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
                pageSize = 20,
                page = 1
            )
        }
    }

    @Test
    fun `load should return middle page with correct keys when loading page 2`() = runTest {
        // Given
        val articles = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "cnn", name = "CNN"),
                author = "Bob Smith",
                title = "Test Article 3",
                description = "Test Description 3",
                url = "https://example.com/article3",
                urlToImage = "https://example.com/image3.jpg",
                publishedAt = "2023-12-01T12:00:00Z",
                content = "Test Content 3"
            )
        )
        val newsResponse = NewsResponse(totalResults = 100, articles = articles)
        coEvery {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
                pageSize = 20,
                page = 2
            )
        } returns Result.Success(newsResponse)

        val loadParams =
            PagingSource.LoadParams.Append(key = 2, loadSize = 20, placeholdersEnabled = false)

        // When
        val result = pagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page

        assertEquals(1, pageResult.data.size)
        assertEquals("Test Article 3", pageResult.data[0].title)

        assertEquals(1, pageResult.prevKey) // Previous page should be 1
        assertEquals(3, pageResult.nextKey) // Next page should be 3

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
                pageSize = 20,
                page = 2
            )
        }
    }

    @Test
    fun `load should return last page with null nextKey when no more articles`() = runTest {
        // Given - Empty articles list indicates last page
        val newsResponse = NewsResponse(totalResults = 0, articles = emptyList())
        coEvery {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
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

        assertTrue(pageResult.data.isEmpty())
        assertEquals(2, pageResult.prevKey) // Previous page should be 2
        assertNull(pageResult.nextKey) // No next page

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
                pageSize = 20,
                page = 3
            )
        }
    }

    @Test
    fun `load should handle null articles list`() = runTest {
        // Given
        val newsResponse = NewsResponse(totalResults = 0, articles = null)
        coEvery {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
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

    @Test
    fun `load should remove duplicate articles by URL`() = runTest {
        // Given - Articles with duplicate URLs
        val articles = listOf(
            ArticleDto(
                source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "Test Article 1",
                description = "Test Description 1",
                url = "https://example.com/article1", // Same URL
                urlToImage = "https://example.com/image1.jpg",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Test Content 1"
            ),
            ArticleDto(
                source = ArticleSourceDto(id = "cnn", name = "CNN"),
                author = "Jane Doe",
                title = "Test Article 2",
                description = "Test Description 2",
                url = "https://example.com/article1", // Duplicate URL
                urlToImage = "https://example.com/image2.jpg",
                publishedAt = "2023-12-01T11:00:00Z",
                content = "Test Content 2"
            ),
            ArticleDto(
                source = ArticleSourceDto(id = "reuters", name = "Reuters"),
                author = "Bob Smith",
                title = "Test Article 3",
                description = "Test Description 3",
                url = "https://example.com/article3", // Unique URL
                urlToImage = "https://example.com/image3.jpg",
                publishedAt = "2023-12-01T12:00:00Z",
                content = "Test Content 3"
            )
        )
        val newsResponse = NewsResponse(totalResults = 100, articles = articles)
        coEvery {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
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
        assertEquals("https://example.com/article1", pageResult.data[0].url)
        assertEquals("https://example.com/article3", pageResult.data[1].url)

        // First article with duplicate URL should be kept
        assertEquals("Test Article 1", pageResult.data[0].title)
        assertEquals("Test Article 3", pageResult.data[1].title)
    }

    @Test
    fun `load should handle articles with totalResults greater than 0 but empty articles list`() =
        runTest {
            // Given - Edge case: totalResults > 0 but articles is empty
            val newsResponse = NewsResponse(totalResults = 100, articles = emptyList())
            coEvery {
                mockNewsRemoteDataSource.getNews(
                    newsParams = newsParams,
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
            assertNull(pageResult.nextKey) // No next page when articles is empty (NewsAPI behavior)
        }

    // ========== load() Error Tests ==========

    @Test
    fun `load should return LoadResult Error when remote data source returns Result Error`() =
        runTest {
            // Given
            val error = Result.Error(
                httpCode = 401,
                errorCode = "apiKeyInvalid",
                errorMessage = "Invalid API key"
            )
            coEvery {
                mockNewsRemoteDataSource.getNews(
                    newsParams = newsParams,
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
                "HttpCode: 401, ErrorCode: apiKeyInvalid, ErrorMessage: Invalid API key",
                exception.message
            )
        }

    @Test
    fun `load should return LoadResult Error when remote data source throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery {
            mockNewsRemoteDataSource.getNews(
                newsParams = newsParams,
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
    fun `load should pass correct parameters to remote data source`() = runTest {
        // Given
        val customParams = NewsParams(country = "gb", source = null, language = "en")
        val customPagingSource = NewsPagingSource(mockNewsRemoteDataSource, customParams)

        val newsResponse = NewsResponse(totalResults = 0, articles = emptyList())
        coEvery {
            mockNewsRemoteDataSource.getNews(
                newsParams = customParams,
                pageSize = 20,
                page = 5
            )
        } returns Result.Success(newsResponse)

        val loadParams =
            PagingSource.LoadParams.Append(key = 5, loadSize = 20, placeholdersEnabled = false)

        // When
        val result = customPagingSource.load(loadParams)

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)

        coVerify(exactly = 1) {
            mockNewsRemoteDataSource.getNews(
                newsParams = customParams,
                pageSize = 20,
                page = 5
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
        coEvery { mockPage.prevKey } returns 2
        coEvery { mockPage.nextKey } returns 4

        val state = mockk<PagingState<Int, Article>>()
        coEvery { state.anchorPosition } returns 10
        coEvery { state.closestPageToPosition(10) } returns mockPage

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertEquals(3, refreshKey) // prevKey + 1 = 2 + 1 = 3
    }

    @Test
    fun `getRefreshKey should return correct key when anchorPosition is valid with nextKey only`() {
        // Given
        val mockPage = mockk<PagingSource.LoadResult.Page<Int, Article>>()
        coEvery { mockPage.prevKey } returns null
        coEvery { mockPage.nextKey } returns 2

        val state = mockk<PagingState<Int, Article>>()
        coEvery { state.anchorPosition } returns 5
        coEvery { state.closestPageToPosition(5) } returns mockPage

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertEquals(1, refreshKey) // nextKey - 1 = 2 - 1 = 1
    }

    @Test
    fun `getRefreshKey should return null when closestPageToPosition returns null`() {
        // Given
        val state = mockk<PagingState<Int, Article>>()
        coEvery { state.anchorPosition } returns 10
        coEvery { state.closestPageToPosition(10) } returns null

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertNull(refreshKey)
    }
}
