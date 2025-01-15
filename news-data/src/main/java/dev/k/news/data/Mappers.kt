package dev.k.news.data

import dev.k.database.models.ArticleDBO
import dev.k.database.models.SourceDBO
import dev.k.news.data.models.Article
import dev.k.news.data.models.Source
import dev.k.newsapi.models.ArticleDTO

internal fun ArticleDBO.toArticle(): Article {
    return Article(
        id = this.id,
        source = Source(
            id = this.source.id,
            name = this.source.name,
        ),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlImage = this.urlImage,
        publishedAt = this.publishedAt,
        content = this.content,
    )
}

internal fun ArticleDTO.toArticleDBO(): ArticleDBO {
    return ArticleDBO(
        source = SourceDBO(
            id = this.source.id,
            name = this.source.name,
        ),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlImage = this.urlImage,
        publishedAt = this.publishedAt,
        content = this.content,
    )
}