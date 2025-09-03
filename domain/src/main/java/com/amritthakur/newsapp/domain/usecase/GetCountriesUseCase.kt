package com.amritthakur.newsapp.domain.usecase

import com.amritthakur.newsapp.domain.common.Outcome
import com.amritthakur.newsapp.domain.common.Result
import com.amritthakur.newsapp.domain.common.toDomainError
import com.amritthakur.newsapp.domain.entity.Country
import com.amritthakur.newsapp.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCountriesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Outcome<List<Country>> {
        return when (val result = newsRepository.getCountries()) {
            is Result.Success -> Outcome.Success(result.data)
            is Result.Error -> Outcome.Error(result.toDomainError())
        }
    }
}