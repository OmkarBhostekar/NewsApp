package com.omkarcodes.newsapp.data

import com.omkarcodes.newsapp.data.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
    ) : NewsResponse

    @GET("/v2/everything")
    suspend fun getCategoryNews(
        @Query("q") category: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
    ) : NewsResponse

}