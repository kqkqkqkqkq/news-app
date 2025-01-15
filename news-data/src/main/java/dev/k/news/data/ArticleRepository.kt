package dev.k.news.data

import dev.k.database.NewsDatabase
import dev.k.database.models.ArticleDBO
import dev.k.news.data.models.Article
import dev.k.newsapi.NewsApi
import dev.k.newsapi.models.ArticleDTO
import dev.k.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ArticleRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(): Flow<RequestResult.Success<List<ArticleDBO>?>> {
        val localCachedArticles = database.articlesDao.getAll()
            .map{ articles -> articles.map { it.toArticle() } }

//        val remoteArticles: Flow<RequestResult.Success<*>>

        TODO("Not impl")
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveResponseToDatabase(checkNotNull(result.getOrThrow()).articles)
                }
            }

        return flow {
            emit(RequestResult.InProgress())
            emit(api.everything().toRequestResult())
        }
            .onEach { requestResult ->
                if (requestResult is RequestResult.Success) {
                    saveResponseToDatabase(checkNotNull(requestResult.data).articles)
                }
            }
    }

    private suspend fun saveResponseToDatabase(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDBO() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything(query)
        TODO("Not impl")
    }
}

sealed class RequestResult<E>(internal val data: E? = null) {

    class InProgress<E>(data: E? = null) : RequestResult<E>(data)
    class Success<E>(data: E) : RequestResult<E>(data)
    class Error<E> : RequestResult<E>()
}

internal fun <T: Any> RequestResult<T?>.requireData() : T = checkNotNull(data)

internal fun <In, Out> RequestResult<In>.map(mapper: (In?) -> Out): RequestResult<Out> {
    val outData = mapper(data)
    return when(this) {
        is RequestResult.Success -> RequestResult.Success(outData)
        is RequestResult.InProgress -> RequestResult.InProgress(outData)
        is RequestResult.Error -> RequestResult.Error()
    }
}

internal fun <T> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Impossible branch")
    }
}