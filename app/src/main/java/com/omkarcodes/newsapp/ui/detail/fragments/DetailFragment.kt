package com.omkarcodes.newsapp.ui.detail.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omkarcodes.newsapp.R
import com.omkarcodes.newsapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail){

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        binding.apply {
            toolbar.apply {
                title = args.title ?: "Latest News"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            webview.apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                args.url?.let {
                    loadUrl(it)
                }
            }
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}