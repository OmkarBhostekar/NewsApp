package com.omkarcodes.newsapp.ui.home.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.omkarcodes.newsapp.R
import com.omkarcodes.newsapp.databinding.FragmentHomeBinding
import com.omkarcodes.newsapp.ui.home.HomeViewModel
import com.omkarcodes.newsapp.ui.home.adapters.HomePagerAdapter
import com.omkarcodes.newsapp.ui.home.adapters.NewsFeedAdapter
import com.omkarcodes.newsapp.utils.Constants.HOME_TABS
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
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fetchRemoteConfig()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.apply {
            tabLayout.apply {
                HOME_TABS.forEach {
                    addTab(newTab().setText(it))
                }
            }
            vpCategories.adapter = HomePagerAdapter(this@HomeFragment, HOME_TABS)

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    vpCategories.setCurrentItem(tab?.position ?: 0,true)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            vpCategories.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })
        }
    }

    //    private fun getAdmobEnabledValue() {
//        admobEnabled = firebaseRemoteConfig.getBoolean("admob_enabled")
//        Toast.makeText(requireContext(), admobEnabled.toString(), Toast.LENGTH_SHORT).show()
//        getNewsData(admobEnabled)
//    }



//    private fun fetchRemoteConfig() {
//        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
//        val remoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
//            .setFetchTimeoutInSeconds(10)
//            .build()
//        firebaseRemoteConfig.setDefaultsAsync(R.xml.admob_enabled_default_value)
//            .addOnCompleteListener {
//                firebaseRemoteConfig.setConfigSettingsAsync(remoteConfigSettings)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful){
//                            firebaseRemoteConfig.fetchAndActivate()
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful)
//                                        getAdmobEnabledValue()
//                                }
//                        }else {
//                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//                            getNewsData(admobEnabled)
//                        }
//                    }
//            }
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}