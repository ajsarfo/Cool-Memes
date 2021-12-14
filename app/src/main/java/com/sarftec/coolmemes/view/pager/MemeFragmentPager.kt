package com.sarftec.coolmemes.view.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.view.fragment.main.FavoriteFragment
import com.sarftec.coolmemes.view.fragment.main.HomeFragment
import com.sarftec.coolmemes.view.fragment.main.SettingsFragment

class MemeFragmentPager (private val activity: FragmentActivity)
        : FragmentStateAdapter(activity){

        override fun getItemCount(): Int {
            return activity.resources.getStringArray(R.array.tab_layout).size
        }

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> HomeFragment()
                1 -> FavoriteFragment()
                else -> SettingsFragment()
            }
        }
}