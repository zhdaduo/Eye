package com.mor.eye.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mor.eye.R
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.BaseFragment
import com.mor.eye.view.home.category.CategoryActivity
import com.mor.eye.view.home.commontab.CommonTabFragment
import com.mor.eye.view.home.community.CommunityFragment
import com.mor.eye.view.home.daily.DailyFragment
import com.mor.eye.view.home.discovery.FindFragment
import com.mor.eye.view.home.recommend.RecommendFragment
import com.mor.eye.view.home.search.SearchActivity
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_tab_title.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
    private val model: HomeViewModel by viewModel()
    private val pages by unsafeLazy { FragmentPagerItems(requireActivity()) }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home, null)

    override fun init() {
        // model.getCategoryOrder()
        initObserveViewModel()
        initListener()
        initTab()
    }

    private fun initObserveViewModel() {
        model.uiLoadData.observeK(this) {}
    }

    private fun initListener() {
        iv_all_category.setOnClickListener { CategoryActivity.open(requireContext()) }
        iv_search.setOnClickListener { SearchActivity.open(requireContext()) }
    }

    private fun initTab() {
        pages.apply {
            val tabs = resources.getStringArray(R.array.tabs)
            val tabsOrder = resources.getIntArray(R.array.tabs_order)
            add(FragmentPagerItem.of(tabs[0], FindFragment::class.java))
            add(FragmentPagerItem.of(tabs[1], RecommendFragment::class.java))
            add(FragmentPagerItem.of(tabs[2], DailyFragment::class.java))
            add(FragmentPagerItem.of(tabs[3], CommunityFragment::class.java))
            for (i in 4 until tabs.size) {
                add(FragmentPagerItem.of(tabs[i], CommonTabFragment::class.java, CommonTabFragment.arguments(tabsOrder[i])))
            }

            view_pager.adapter = FragmentPagerItemAdapter(activity?.supportFragmentManager, pages)
            view_pager.currentItem = 1
            view_pager.offscreenPageLimit = 1
            tab_view_pager.setViewPager(view_pager)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pages.clear()
    }
}