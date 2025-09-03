package com.amritthakur.newsapp.domain.usecase

import androidx.paging.PagingData
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.domain.entity.NewsParams
import com.amritthakur.newsapp.domain.repository.NewsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertSame
import org.junit.Test

class GetNewsUseCaseTest {

    private val mockRepository = mockk<NewsRepository>()
    private val getNewsUseCase = GetNewsUseCase(mockRepository)

    @Test
    fun `invoke with default params should delegate to repository with default params`() {
        // Given
        val expectedFlow = mockk<Flow<PagingData<Article>>>()
        every { mockRepository.getNews(NewsParams()) } returns expectedFlow

        // When
        val result = getNewsUseCase.invoke()

        // Then
        assertSame(expectedFlow, result)
        verify(exactly = 1) { mockRepository.getNews(NewsParams()) }
    }

    @Test
    fun `invoke with custom params should delegate to repository with same params`() {
        // Given
        val customParams = NewsParams(
            source = "bbc-news",
            country = "us",
            language = "en"
        )
        val expectedFlow = mockk<Flow<PagingData<Article>>>()
        every { mockRepository.getNews(customParams) } returns expectedFlow

        // When
        val result = getNewsUseCase.invoke(customParams)

        // Then
        assertSame(expectedFlow, result)
        verify(exactly = 1) { mockRepository.getNews(customParams) }
    }

    @Test
    fun `invoke with partial params should delegate to repository with same partial params`() {
        // Given
        val partialParams = NewsParams(
            source = null,
            country = "gb",
            language = null
        )
        val expectedFlow = mockk<Flow<PagingData<Article>>>()
        every { mockRepository.getNews(partialParams) } returns expectedFlow

        // When
        val result = getNewsUseCase.invoke(partialParams)

        // Then
        assertSame(expectedFlow, result)
        verify(exactly = 1) { mockRepository.getNews(partialParams) }
    }
}