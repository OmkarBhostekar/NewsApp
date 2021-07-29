package com.omkarcodes.newsapp.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.omkarcodes.newsapp.R
import com.omkarcodes.newsapp.databinding.FragmentHomeBinding
import com.omkarcodes.newsapp.ui.home.HomeViewModel
import com.omkarcodes.newsapp.ui.home.adapters.NewsFeedAdapter
import com.omkarcodes.newsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home){

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    lateinit var newsFeedAdapter: NewsFeedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        newsFeedAdapter = NewsFeedAdapter()

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsFeedAdapter
        }

        getNewsData()
    }

    private fun getNewsData() {
        lifecycleScope.launch {
            viewModel.getNews("in").collectLatest { pagingData ->
                newsFeedAdapter.submitData(viewLifecycleOwner.lifecycle,pagingData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}