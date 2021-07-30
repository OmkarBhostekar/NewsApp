package com.omkarcodes.newsapp.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.omkarcodes.newsapp.R
import com.omkarcodes.newsapp.data.models.Article
import com.omkarcodes.newsapp.data.models.NewsType
import com.omkarcodes.newsapp.databinding.ItemFeedAdBinding
import com.omkarcodes.newsapp.databinding.ItemNewsBinding
import com.omkarcodes.newsapp.utils.toTimeAgo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

class NewsFeedAdapter(
    val listener: OnClickListener,
    val lifecycleScope: LifecycleCoroutineScope
) : PagingDataAdapter<Article,NewsFeedAdapter.NewsViewHolder>(NEWS_COMPARATOR) {

    companion object {
        const val NEWS_VIEW_TYPE = 1
        const val NATIVE_AD_VIEW_TYPE = 2
        val NEWS_COMPARATOR = object : DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if(getItem(position)?.type == NewsType.AD) NATIVE_AD_VIEW_TYPE else NEWS_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return if (viewType == NEWS_VIEW_TYPE)
            NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        else
            NewsViewHolder(ItemFeedAdBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class NewsViewHolder : RecyclerView.ViewHolder {
        private var itemNewsBinding: ItemNewsBinding? = null
        private var itemFeedAdBinding: ItemFeedAdBinding? = null

        constructor(binding: ItemNewsBinding) : super(binding.root){
            itemNewsBinding = binding
        }

        constructor(binding: ItemFeedAdBinding) : super(binding.root){
            itemFeedAdBinding = binding
        }

        fun bind(position: Int){
            val viewType = getItemViewType(position)
            if (viewType == NEWS_VIEW_TYPE){
                // Show News
                val news = getItem(position)
                itemNewsBinding?.let { binding ->
                    binding.root.setOnClickListener { listener.onNewsClick(news?.url,news?.title) }
                    Glide.with(binding.root.context).load(news?.urlToImage).placeholder(R.drawable.ic_news).into(binding.ivThumbnail)
                    binding.tvTitle.text = news?.title
                    binding.tvSource.text = "- ${news?.source?.name}"
                    binding.tvTimestamp.text = news?.publishedAt?.toTimeAgo()
                }
            }else{
                // Show ad
                itemFeedAdBinding?.let {
                    lifecycleScope.launch(Dispatchers.Main) {
                        val adLoader = AdLoader.Builder(itemFeedAdBinding!!.root.context,"ca-app-pub-3940256099942544/2247696110")
                            .forNativeAd { nativeAd ->
                                val styles = NativeTemplateStyle.Builder()
                                    .build()
                                itemFeedAdBinding!!.adView.setStyles(styles)
                                itemFeedAdBinding!!.adView.setNativeAd(nativeAd)
                            }
                            .withAdListener(object: AdListener(){
                                override fun onAdFailedToLoad(p0: LoadAdError) {
                                    super.onAdFailedToLoad(p0)
                                }
                            })
                            .withNativeAdOptions(NativeAdOptions.Builder().build())
                            .build()

                        adLoader.loadAd(AdRequest.Builder().build())
                    }
                }
            }
        }
    }

    interface OnClickListener{
        fun onNewsClick(url: String?,title: String?)
    }
}