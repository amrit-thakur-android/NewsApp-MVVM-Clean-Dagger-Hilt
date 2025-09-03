package com.amritthakur.newsapp.domain.usecase

import androidx.paging.PagingData
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.domain.repository.NewsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertSame
import org.junit.Test

class SearchNewsUseCaseTest {

    private val mockRepository = mockk<NewsRepository>()
    private val searchNewsUseCase = SearchNewsUseCase(mockRepository)

    @Test
    fun `invoke with normal query should delegate to repository with same query`() {
        // Given
        val query = "android development"
        val expectedFlow = mockk<Flow<PagingData<Article>>>()
        every { mockRepository.searchNews(query) } returns expectedFlow

        // When
        val result = searchNewsUseCase.invoke(query)

        // Then
        assertSame(expectedFlow, result)
        verify(exactly = 1) { mockRepository.searchNews(query) }
    }

    @Test
    fun `invoke with empty query should delegate to repository with empty query`() {
        // Given
        val query = ""
        val expectedFlow = mockk<Flow<PagingData<Article>>>()
        every { mockRepository.searchNews(query) } returns expectedFlow

        // When
        val result = searchNewsUseCase.invoke(query)

        // Then
        assertSame(expectedFlow, result)
        verify(exactly = 1) { mockRepository.searchNews(query) }
    }

    @Test
    fun `invoke with special characters query should delegate to repository with same query`() {
        // Given
        val query = "covid-19 & vaccine news!"
        val expectedFlow = mockk<Flow<PagingData<Article>>>()
        every { mockRepository.searchNews(query) } returns expectedFlow

        // When
        val result = searchNewsUseCase.invoke(query)

        // Then
        assertSame(expectedFlow, result)
        verify(exactly = 1) { mockRepository.searchNews(query) }
    }
}
