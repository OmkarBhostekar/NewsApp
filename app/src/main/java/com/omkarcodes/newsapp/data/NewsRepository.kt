package com.omkarcodes.newsapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.omkarcodes.newsapp.utils.Constants
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApi
) {

    fun getNews(country: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(api,country,Constants.API_KEY)
            }
        ).flow
}