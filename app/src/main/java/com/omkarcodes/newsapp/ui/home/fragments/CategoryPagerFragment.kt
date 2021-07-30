package com.omkarcodes.newsapp.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.omkarcodes.newsapp.R
import com.omkarcodes.newsapp.data.models.Article
import com.omkarcodes.newsapp.databinding.FragmentCategoryBinding
import com.omkarcodes.newsapp.ui.MainActivity
import com.omkarcodes.newsapp.ui.home.HomeViewModel
import com.omkarcodes.newsapp.ui.home.adapters.NewsFeedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryPagerFragment : Fragment(R.layout.fragment_category), NewsFeedAdapter.OnClickListener{

    fun newInstance(type: String,admobEnabled: Boolean): CategoryPagerFragment{
        val args = Bundle()
        args.putString("type",type)
        args.putBoolean("admobEnabled",admobEnabled)
        val fragment = CategoryPagerFragment()
        fragment.arguments = args
        return fragment
    }

    private var _binding: FragmentCategoryBinding? = null
    private val binding: FragmentCategoryBinding
        get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    lateinit var newsFeedAdapter: NewsFeedAdapter
    private var news: PagingData<Article>? = null
    private var newsLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)

        val admobEnabled = arguments?.getBoolean("admobEnabled",false)!!

        val type = arguments?.getString("type","top")

        newsFeedAdapter = NewsFeedAdapter(this,lifecycleScope)

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsFeedAdapter
            setHasFixedSize(true)
            newsFeedAdapter.addLoadStateListener { loadState ->
                binding.progressBar.isVisible = loadState.source.refresh == LoadState.Loading
            }
        }

        if (newsLoaded){
            lifecycleScope.launch {
                newsFeedAdapter.submitData(viewLifecycleOwner.lifecycle,news!!)
            }
        }else{
            if (type == "Top Headlines")
                getTopHeadlines(admobEnabled,(activity as MainActivity).countryCode)
            else
                getCategoryNews(type!!,admobEnabled)
        }

    }

    private fun getCategoryNews(type: String,admobEnabled: Boolean) {
        lifecycleScope.launch {
            viewModel.getCategoryNews(category = type,admobEnabled = admobEnabled).collectLatest { pagingData ->
                newsLoaded = true
                news = pagingData
                newsFeedAdapter.submitData(viewLifecycleOwner.lifecycle,news!!)
            }
        }
    }

    private fun getTopHeadlines(admobEnabled: Boolean,country: String) {
        lifecycleScope.launch {
            viewModel.getTopHeadlines(country = country,admobEnabled = admobEnabled).collectLatest { pagingData ->
                newsLoaded = true
                news = pagingData
                newsFeedAdapter.submitData(viewLifecycleOwner.lifecycle,news!!)
            }
        }
    }

    override fun onNewsClick(url: String?,title: String?) {
        findNavController().navigate(HomeFragmentDirections
            .actionHomeFragmentToDetailFragment(url = url, title = title))
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}