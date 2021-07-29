package com.omkarcodes.newsapp.ui.home.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.omkarcodes.newsapp.ui.home.fragments.CategoryPagerFragment

class HomePagerAdapter(fragment: Fragment,val tabs: List<String>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return CategoryPagerFragment().newInstance(type = tabs[position])
    }
}