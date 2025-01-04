package dev.k.newsapi

import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dev.k.newsapi.models.Article
import dev.k.newsapi.models.Language
import dev.k.newsapi.models.Response
import dev.k.newsapi.models.SortBy
import dev.k.newsapi.utils.NewsApiKeyInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

/**
 * [API Documentation](https://newsapi.org/docs/get-started)
 */
interface NewsApi {
    /**
     * API [source](https://newsapi.org/docs/endpoints/everything)
     */
    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") languages: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Result<Response<Article>>
}

fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json,
): NewsApi {
    return retrofit(baseUrl, apiKey, okHttpClient, json).create()
}

private fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient?,
    json: Json,
): Retrofit {
    val jsonConverterFactory
        = json.asConverterFactory(MediaType.get("application/json"))

    val finallyOkHttpClient: OkHttpClient =
        (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(NewsApiKeyInterceptor(apiKey)).build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(finallyOkHttpClient)
        .build()
}