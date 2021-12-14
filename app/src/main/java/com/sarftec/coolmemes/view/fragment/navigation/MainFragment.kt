package com.sarftec.coolmemes.view.fragment.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sarftec.coolmemes.R
import com.sarftec.coolmemes.databinding.FragmentMainBinding
import com.sarftec.coolmemes.view.pager.MemeFragmentPager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var layoutBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentMainBinding.inflate(
            layoutInflater,
            container,
            false
        )
        setupViewPager()
        setupTabLayout()
        return layoutBinding.root
    }
    private fun setupTabLayout() {
        val tabHeadings = resources.getStringArray(R.array.tab_layout)
        TabLayoutMediator(
            layoutBinding.tabLayout,
            layoutBinding.viewPager
        ) { tab, position ->
            tab.text = tabHeadings[position]
        }.attach()
    }

    private fun setupViewPager() {
        layoutBinding.viewPager.adapter = MemeFragmentPager(requireActivity())
    }
}