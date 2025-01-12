package dev.k.newsapi.models

import kotlinx.serialization.SerialName

enum class SortBy {
    @SerialName("relevancy") RELEVANCY,
    @SerialName("popularity") POPULARITY,
    @SerialName("publishedAt") PUBLISHED_AT,
}