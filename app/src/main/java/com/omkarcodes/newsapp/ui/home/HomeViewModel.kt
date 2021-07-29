package com.omkarcodes.newsapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.omkarcodes.newsapp.data.NewsRepository
import com.omkarcodes.newsapp.data.models.Article
import com.omkarcodes.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun getTopHeadlines(country: String,admobEnabled: Boolean)
        = repository.getTopHeadlines(country,admobEnabled).cachedIn(viewModelScope)

    fun getCategoryNews(category: String,admobEnabled: Boolean)
        = repository.getCategoryNews(category,admobEnabled).cachedIn(viewModelScope)

}