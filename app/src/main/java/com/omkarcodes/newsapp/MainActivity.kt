package com.omkarcodes.newsapp

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAdOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)

        val adLoader = AdLoader.Builder(this,"ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                val styles = NativeTemplateStyle.Builder()
                    .build()
                val templateView = findViewById<TemplateView>(R.id.ad)
                templateView.setStyles(styles)
                templateView.setNativeAd(nativeAd)
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