package com.omkarcodes.newsapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omkarcodes.newsapp.data.models.Article
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class NewsPagingSource(
    private val api: NewsApi,
    private val country: String,
    private val apiKey: String
) : PagingSource<Int,Article>(){
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: 0
        return try {
            val response = api.getNews(country,apiKey,position)
            val articles = response.articles
            LoadResult.Page(
                data = articles,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (position != getTotalPageCount(response.totalResults)) position + 1 else null
            )
        }catch (e: SocketTimeoutException){
            LoadResult.Error(e)
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }

    private fun getTotalPageCount(totalResults: Int): Int {
        var count = totalResults / 20
        if(totalResults % 20 != 0) count++
        return count
    }
}