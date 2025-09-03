package com.amritthakur.newsapp.data.remote.datasource

import com.amritthakur.newsapp.data.remote.api.NewsApiService
import com.amritthakur.newsapp.data.remote.dto.ArticleDto
import com.amritthakur.newsapp.data.remote.dto.ArticleSourceDto
import com.amritthakur.newsapp.data.remote.dto.SourceDto
import com.amritthakur.newsapp.data.remote.response.NewsResponse
import com.amritthakur.newsapp.data.remote.response.SourcesResponse
import com.amritthakur.newsapp.data.remote.util.toError
import com.amritthakur.newsapp.data.util.NetworkConnectivityManager
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.entity.NewsParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class NewsRemoteDataSourceTest {

    private val mockNewsApiService = mockk<NewsApiService>()
    private val mockNetworkConnectivityManager = mockk<NetworkConnectivityManager>()
    private val newsRemoteDataSource =
        NewsRemoteDataSource(mockNewsApiService, mockNetworkConnectivityManager)

    @Before
    fun setup() {
        // Mock the extension functions from ErrorUtil
        mockkStatic("com.amritthakur.newsapp.data.remote.util.ErrorUtilKt")

        // Mock network connectivity to return true by default
        every { mockNetworkConnectivityManager.isNetworkAvailable() } returns true
    }

    @After
    fun tearDown() {
        unmockkStatic("com.amritthakur.newsapp.data.remote.util.ErrorUtilKt")
    }

    // ========== getNews Tests ==========

    @Test
    fun `getNews should return success when API returns valid response`() = runTest {
        // Given
        val params = NewsParams(country = "us", source = "bbc-news", language = "en")
        val pageSize = 20
        val page = 1
        val mockNewsResponse = NewsResponse(
            totalResults = 100,
            articles = listOf(
                ArticleDto(
                    source = ArticleSourceDto(id = "bbc-news", name = "BBC News"),
                    author = "John Doe",
                    title = "Test Article",
                    description = "Test Description",
                    url = "https://example.com",
                    urlToImage = "https://example.com/image.jpg",
                    publishedAt = "2023-12-01T10:00:00Z",
                    content = "Test Content"
                )
            )
        )
        val mockResponse = mockk<Response<NewsResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns mockNewsResponse

        coEvery {
            mockNewsApiService.getNews(
                country = params.country,
                source = params.source,
                language = params.language,
                pageSize = pageSize,
                page = page
            )
        } returns mockResponse

        // When
        val result = newsRemoteDataSource.getNews(params, pageSize, page)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(mockNewsResponse, (result as Result.Success).data)
        coVerify(exactly = 1) {
            mockNewsApiService.getNews(
                country = "us",
                source = "bbc-news",
                language = "en",
                pageSize = 20,
                page = 1
            )
        }
    }

    @Test
    fun `getNews should return parsing error when response body is null`() = runTest {
        // Given
        val params = NewsParams()
        val mockResponse = mockk<Response<NewsResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null

        coEvery {
            mockNewsApiService.getNews(
                country = params.country,
                source = params.source,
                language = params.language,
                pageSize = null,
                page = null
            )
        } returns mockResponse

        // When
        val result = newsRemoteDataSource.getNews(params)

        // Then
        assertTrue(result is Result.Error)
        val error = result as Result.Error
        assertEquals(-2, error.httpCode)
        assertEquals("parsingError", error.errorCode)
        assertEquals("Empty response body", error.errorMessage)
    }

    @Test
    fun `getNews should call response toError when API returns error response`() = runTest {
        // Given
        val params = NewsParams()
        val mockResponse = mockk<Response<NewsResponse>>()
        val expectedError = Result.Error(400, "parameterInvalid", "Invalid parameter")

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.toError() } returns expectedError

        coEvery {
            mockNewsApiService.getNews(
                country = params.country,
                source = params.source,
                language = params.language,
                pageSize = null,
                page = null
            )
        } returns mockResponse

        // When
        val result = newsRemoteDataSource.getNews(params)

        // Then
        assertEquals(expectedError, result)
    }

    @Test
    fun `getNews should call exception toError when API throws exception`() = runTest {
        // Given
        val params = NewsParams()
        val exception = IOException("Network error")
        val expectedError = Result.Error(-1, "IOException", "Network error")

        every { exception.toError() } returns expectedError
        coEvery {
            mockNewsApiService.getNews(
                country = params.country,
                source = params.source,
                language = params.language,
                pageSize = null,
                page = null
            )
        } throws exception

        // When
        val result = newsRemoteDataSource.getNews(params)

        // Then
        assertEquals(expectedError, result)
    }

    @Test
    fun `getNews should pass default params correctly`() = runTest {
        // Given
        val defaultParams = NewsParams() // All fields null
        val mockResponse = mockk<Response<NewsResponse>>()
        val mockNewsResponse = NewsResponse(totalResults = 0, articles = emptyList())

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns mockNewsResponse

        coEvery {
            mockNewsApiService.getNews(
                country = null,
                source = null,
                language = null,
                pageSize = null,
                page = null
            )
        } returns mockResponse

        // When
        val result = newsRemoteDataSource.getNews(defaultParams)

        // Then
        assertTrue(result is Result.Success)
        coVerify(exactly = 1) {
            mockNewsApiService.getNews(
                country = null,
                source = null,
                language = null,
                pageSize = null,
                page = null
            )
        }
    }

    @Test
    fun `getNews should pass all parameter combinations correctly`() = runTest {
        // Given
        val testCases = listOf(
            NewsParams(country = "us", source = null, language = null),
            NewsParams(country = null, source = "bbc-news", language = null),
            NewsParams(country = null, source = null, language = "en"),
            NewsParams(country = "us", source = "bbc-news", language = null),
            NewsParams(country = "us", source = null, language = "en"),
            NewsParams(country = null, source = "bbc-news", language = "en"),
            NewsParams(country = "us", source = "bbc-news", language = "en")
        )

        val mockResponse = mockk<Response<NewsResponse>>()
        val mockNewsResponse = NewsResponse(totalResults = 0, articles = emptyList())
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns mockNewsResponse

        testCases.forEach { params ->
            coEvery {
                mockNewsApiService.getNews(
                    country = params.country,
                    source = params.source,
                    language = params.language,
                    pageSize = null,
                    page = null
                )
            } returns mockResponse

            // When
            val result = newsRemoteDataSource.getNews(params)

            // Then
            assertTrue("Failed for params: $params", result is Result.Success)
            coVerify(exactly = 1) {
                mockNewsApiService.getNews(
                    country = params.country,
                    source = params.source,
                    language = params.language,
                    pageSize = null,
                    page = null
                )
            }
        }
    }

    // ========== getSources Tests ==========

    @Test
    fun `getSources should return success when API returns valid response`() = runTest {
        // Given
        val mockSourcesResponse = SourcesResponse(
            sources = listOf(
                SourceDto(
                    id = "bbc-news",
                    name = "BBC News",
                    description = "BBC News description",
                    url = "http://www.bbc.co.uk/news",
                    category = "general",
                    language = "en",
                    country = "gb"
                )
            )
        )
        val mockResponse = mockk<Response<SourcesResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns mockSourcesResponse

        coEvery { mockNewsApiService.getSources() } returns mockResponse

        // When
        val result = newsRemoteDataSource.getSources()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(mockSourcesResponse, (result as Result.Success).data)
        coVerify(exactly = 1) { mockNewsApiService.getSources() }
    }

    @Test
    fun `getSources should return parsing error when response body is null`() = runTest {
        // Given
        val mockResponse = mockk<Response<SourcesResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null

        coEvery { mockNewsApiService.getSources() } returns mockResponse

        // When
        val result = newsRemoteDataSource.getSources()

        // Then
        assertTrue(result is Result.Error)
        val error = result as Result.Error
        assertEquals(-2, error.httpCode)
        assertEquals("parsingError", error.errorCode)
        assertEquals("Empty response body", error.errorMessage)
    }

    @Test
    fun `getSources should call response toError when API returns error response`() = runTest {
        // Given
        val mockResponse = mockk<Response<SourcesResponse>>()
        val expectedError = Result.Error(401, "apiKeyInvalid", "Invalid API key")

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.toError() } returns expectedError

        coEvery { mockNewsApiService.getSources() } returns mockResponse

        // When
        val result = newsRemoteDataSource.getSources()

        // Then
        assertEquals(expectedError, result)
    }

    @Test
    fun `getSources should call exception toError when API throws exception`() = runTest {
        // Given
        val exception = IOException("Network error")
        val expectedError = Result.Error(-1, "IOException", "Network error")

        every { exception.toError() } returns expectedError
        coEvery { mockNewsApiService.getSources() } throws exception

        // When
        val result = newsRemoteDataSource.getSources()

        // Then
        assertEquals(expectedError, result)
    }

    // ========== searchNews Tests ==========

    @Test
    fun `searchNews should return success when API returns valid response`() = runTest {
        // Given
        val query = "android development"
        val pageSize = 20
        val page = 1
        val mockNewsResponse = NewsResponse(
            totalResults = 50,
            articles = listOf(
                ArticleDto(
                    source = ArticleSourceDto(id = "techcrunch", name = "TechCrunch"),
                    author = "Jane Doe",
                    title = "Android Development News",
                    description = "Latest Android news",
                    url = "https://techcrunch.com/android",
                    urlToImage = "https://techcrunch.com/image.jpg",
                    publishedAt = "2023-12-01T12:00:00Z",
                    content = "Android content"
                )
            )
        )
        val mockResponse = mockk<Response<NewsResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns mockNewsResponse

        coEvery {
            mockNewsApiService.searchNews(
                query = query,
                pageSize = pageSize,
                page = page
            )
        } returns mockResponse

        // When
        val result = newsRemoteDataSource.searchNews(query, pageSize, page)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(mockNewsResponse, (result as Result.Success).data)
        coVerify(exactly = 1) {
            mockNewsApiService.searchNews(
                query = "android development",
                pageSize = 20,
                page = 1
            )
        }
    }

    @Test
    fun `searchNews should return parsing error when response body is null`() = runTest {
        // Given
        val query = "test query"
        val mockResponse = mockk<Response<NewsResponse>>()
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns null

        coEvery {
            mockNewsApiService.searchNews(
                query = query,
                pageSize = null,
                page = null
            )
        } returns mockResponse

        // When
        val result = newsRemoteDataSource.searchNews(query)

        // Then
        assertTrue(result is Result.Error)
        val error = result as Result.Error
        assertEquals(-2, error.httpCode)
        assertEquals("parsingError", error.errorCode)
        assertEquals("Empty response body", error.errorMessage)
    }

    @Test
    fun `searchNews should call response toError when API returns error response`() = runTest {
        // Given
        val query = "test query"
        val mockResponse = mockk<Response<NewsResponse>>()
        val expectedError = Result.Error(429, "rateLimited", "Rate limit exceeded")

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.toError() } returns expectedError

        coEvery {
            mockNewsApiService.searchNews(
                query = query,
                pageSize = null,
                page = null
            )
        } returns mockResponse

        // When
        val result = newsRemoteDataSource.searchNews(query)

        // Then
        assertEquals(expectedError, result)
    }

    @Test
    fun `searchNews should call exception toError when API throws exception`() = runTest {
        // Given
        val query = "test query"
        val exception = IOException("Network timeout")
        val expectedError = Result.Error(-1, "IOException", "Network timeout")

        every { exception.toError() } returns expectedError
        coEvery {
            mockNewsApiService.searchNews(
                query = query,
                pageSize = null,
                page = null
            )
        } throws exception

        // When
        val result = newsRemoteDataSource.searchNews(query)

        // Then
        assertEquals(expectedError, result)
    }

    @Test
    fun `searchNews should pass parameters correctly with different queries`() = runTest {
        // Given
        val testQueries = listOf(
            "android",
            "kotlin programming",
            "covid-19 & vaccine",
            "",
            "special chars !@#$%"
        )
        val pageSize = 10
        val page = 2

        val mockResponse = mockk<Response<NewsResponse>>()
        val mockNewsResponse = NewsResponse(totalResults = 0, articles = emptyList())
        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body() } returns mockNewsResponse

        testQueries.forEach { query ->
            coEvery {
                mockNewsApiService.searchNews(
                    query = query,
                    pageSize = pageSize,
                    page = page
                )
            } returns mockResponse

            // When
            val result = newsRemoteDataSource.searchNews(query, pageSize, page)

            // Then
            assertTrue("Failed for query: '$query'", result is Result.Success)
            coVerify(exactly = 1) {
                mockNewsApiService.searchNews(
                    query = query,
                    pageSize = pageSize,
                    page = page
                )
            }
        }
    }

    // ========== Network Connectivity Tests ==========

    @Test
    fun `getNews should return network error when no internet connection`() = runTest {
        // Given
        every { mockNetworkConnectivityManager.isNetworkAvailable() } returns false
        val params = NewsParams(country = "us")

        // When
        val result = newsRemoteDataSource.getNews(params)

        // Then
        assertTrue("Expected network error", result is Result.Error)
        val error = result as Result.Error
        assertEquals(-1, error.httpCode)
        assertEquals("NetworkError", error.errorCode)
        assertEquals("No internet connection available", error.errorMessage)
    }

    @Test
    fun `getSources should return network error when no internet connection`() = runTest {
        // Given
        every { mockNetworkConnectivityManager.isNetworkAvailable() } returns false

        // When
        val result = newsRemoteDataSource.getSources()

        // Then
        assertTrue("Expected network error", result is Result.Error)
        val error = result as Result.Error
        assertEquals(-1, error.httpCode)
        assertEquals("NetworkError", error.errorCode)
        assertEquals("No internet connection available", error.errorMessage)
    }

    @Test
    fun `searchNews should return network error when no internet connection`() = runTest {
        // Given
        every { mockNetworkConnectivityManager.isNetworkAvailable() } returns false
        val query = "test"

        // When
        val result = newsRemoteDataSource.searchNews(query)

        // Then
        assertTrue("Expected network error", result is Result.Error)
        val error = result as Result.Error
        assertEquals(-1, error.httpCode)
        assertEquals("NetworkError", error.errorCode)
        assertEquals("No internet connection available", error.errorMessage)
    }
}