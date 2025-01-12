package dev.k.news.main

import dev.k.news.data.ArticleRepository


class GetAllArticlesUseCase(private val repository: ArticleRepository) {

    operator suspend fun invoke() = repository.getAll()

}