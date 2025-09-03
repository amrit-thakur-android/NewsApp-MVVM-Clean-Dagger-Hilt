package com.amritthakur.newsapp.domain.usecase

import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.common.toDomainError
import com.amritthakur.newsapp.domain.entity.Source
import com.amritthakur.newsapp.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSourcesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(): Outcome<List<Source>> {
        return when (val result = newsRepository.getSources()) {
            is Result.Success -> Outcome.Success(result.data)
            is Result.Error -> Outcome.Error(result.toDomainError())
        }
    }
}
