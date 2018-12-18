package com.mor.eye.view.home

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mor.eye.R
import com.mor.eye.ui.ColorFilterImageView
import com.mor.eye.util.ktx.SimplePageChangeListener
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.BaseFragment
import com.mor.eye.view.home.category.CategoryFragment
import com.mor.eye.view.home.commontab.CommonTabFragment
import com.mor.eye.view.home.community.CommunityFragment
import com.mor.eye.view.home.daily.DailyFragment
import com.mor.eye.view.home.discovery.FindFragment
import com.mor.eye.view.home.recommend.RecommendFragment
import com.mor.eye.view.home.search.SearchFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.shuyu.gsyvideoplayer.GSYVideoManager

class HomeFragment : BaseFragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var smartTabLayout: SmartTabLayout

    private val pages by unsafeLazy { FragmentPagerItems(requireActivity()) }
    private val listener by unsafeLazy {
        SimplePageChangeListener().selected {
            //当页签切换的时候，如果有播放视频，则释放资源
            GSYVideoManager.releaseAllVideos()
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun getToolBarResId(): Int = R.layout.toolbar_home_tab_title

    override fun initToolBar(rootView: View) {
        toolbar = rootView.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val ivAllCategory = rootView.findViewById<ColorFilterImageView>(R.id.iv_all_category)
        val ivSearch = rootView.findViewById<ColorFilterImageView>(R.id.iv_search)
        ivAllCategory.setOnClickListener { (parentFragment as MainFragment).startBrotherFragment(CategoryFragment.newInstance()) }
        ivSearch.setOnClickListener { (parentFragment as MainFragment).startBrotherFragment(SearchFragment.newInstance()) }
    }

    override fun initView(rootView: View) {
        viewPager = rootView.findViewById(R.id.view_pager)
        smartTabLayout = rootView.findViewById(R.id.tab_view_pager)
        initTab()
    }

    override fun initLogic() {

    }

    private fun initTab() {
        viewPager.addOnPageChangeListener(listener)
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

            viewPager.adapter = FragmentPagerItemAdapter(activity?.supportFragmentManager, pages)
            viewPager.currentItem = 1
            viewPager.offscreenPageLimit = 1
            smartTabLayout.setViewPager(viewPager)
        }
    }

    override fun showToolBar(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.removeOnPageChangeListener(listener)
        pages.clear()
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}