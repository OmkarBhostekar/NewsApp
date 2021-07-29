package com.omkarcodes.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omkarcodes.newsapp.data.NewsApi
import com.omkarcodes.newsapp.data.models.Article
import com.omkarcodes.newsapp.data.models.NewsType
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class NewsPagingSource(
    private val api: NewsApi,
    private val country: String = "in",
    private val apiKey: String,
    private val admobEnabled: Boolean,
    private val isTopHeadlines: Boolean = true,
    private val category: String = "top"
) : PagingSource<Int,Article>(){
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: 1
        return try {
            val response = if (isTopHeadlines)
                api.getTopHeadlines(country,apiKey,position)
            else
                api.getCategoryNews(category,apiKey,position)

            val articles = response.articles
            val newList = mutableListOf<Article>()
            if (admobEnabled){
                articles.chunked(5).forEach {
                    newList.addAll(it)
                    newList.add(Article(type = NewsType.AD))
                }
            }else{
                newList.addAll(articles)
            }
            LoadResult.Page(
                data = newList,
                prevKey = if (position == 1) null else position - 1,
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