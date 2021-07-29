package com.omkarcodes.newsapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.omkarcodes.newsapp.data.paging.NewsPagingSource
import com.omkarcodes.newsapp.utils.Constants
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApi
) {

    fun getTopHeadlines(country: String,admobEnabled: Boolean) =
        Pager(
            config = PagingConfig(
                pageSize = if (admobEnabled) 24 else 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    api = api,
                    country = country,
                    apiKey = Constants.API_KEY,
                    admobEnabled = admobEnabled)
            }
        ).flow

    fun getCategoryNews(category: String,admobEnabled: Boolean) =
        Pager(
            config = PagingConfig(
                pageSize = if (admobEnabled) 24 else 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    api = api,
                    apiKey = Constants.API_KEY,
                    admobEnabled = admobEnabled,
                    category = category,
                    isTopHeadlines = false
                )
            }
        ).flow
}