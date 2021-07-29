package com.omkarcodes.newsapp.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omkarcodes.newsapp.R
import com.omkarcodes.newsapp.databinding.FragmentCategoryBinding
import com.omkarcodes.newsapp.ui.home.HomeViewModel
import com.omkarcodes.newsapp.ui.home.adapters.NewsFeedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryPagerFragment : Fragment(R.layout.fragment_category), NewsFeedAdapter.OnClickListener{

    fun newInstance(type: String): CategoryPagerFragment{
        val args = Bundle()
        args.putString("type",type)
        val fragment = this
        fragment.arguments = args
        return fragment
    }

    private var _binding: FragmentCategoryBinding? = null
    private val binding: FragmentCategoryBinding
        get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    lateinit var newsFeedAdapter: NewsFeedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryBinding.bind(view)

        val type = arguments?.getString("type","top")

        newsFeedAdapter = NewsFeedAdapter(this)

        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsFeedAdapter
            setHasFixedSize(true)
        }

        if (type == "Top Headlines")
            getTopHeadlines(false)
        else
            getCategoryNews(type!!,false)
    }

    private fun getCategoryNews(type: String,admobEnabled: Boolean) {
        lifecycleScope.launch {
            viewModel.getCategoryNews(category = type,admobEnabled = admobEnabled).collectLatest { pagingData ->
                newsFeedAdapter.submitData(viewLifecycleOwner.lifecycle,pagingData)
            }
        }
    }

    private fun getTopHeadlines(admobEnabled: Boolean) {
        lifecycleScope.launch {
            viewModel.getTopHeadlines(country = "in",admobEnabled = admobEnabled).collectLatest { pagingData ->
                newsFeedAdapter.submitData(viewLifecycleOwner.lifecycle,pagingData)
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