package com.mor.eye.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mor.eye.R
import com.mor.eye.ui.bottombar.SpecialTab
import com.mor.eye.ui.bottombar.SpecialTabRound
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.home.mime.MimeFragment
import com.mor.eye.view.home.newcommunity.NewCommunityFragment
import com.mor.eye.view.home.notification.NotificationFragment
import me.majiajie.pagerbottomtabstrip.NavigationController
import me.majiajie.pagerbottomtabstrip.PageNavigationView
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem
import me.yokeyword.fragmentation.SupportFragment

class MainFragment: SupportFragment() {
    private val homeFragment: HomeFragment by unsafeLazy { HomeFragment.newInstance() }
    private val communityFragment: NewCommunityFragment by unsafeLazy { NewCommunityFragment.newInstance() }
    private val notificationFragment: NotificationFragment by unsafeLazy { NotificationFragment.newInstance() }
    private val mimeFragment: MimeFragment by unsafeLazy { MimeFragment.newInstance() }
    private lateinit var navigationController: NavigationController
    private lateinit var bottomTab: PageNavigationView
    private val mFragments = ArrayList<SupportFragment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        initView(view)
        return view
    }

    private fun initView(rootView: View) {
        bottomTab = rootView.findViewById(R.id.bottomTab)
        initFragments()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        navigationController = bottomTab.custom()
                .addItem(newItem(R.mipmap.ic_tab_strip_icon_feed,R.mipmap.ic_tab_strip_icon_feed_selected, "首页"))
                .addItem(newItem(R.mipmap.ic_tab_strip_icon_follow,R.mipmap.ic_tab_strip_icon_follow_selected, "社区"))
                //.addItem(newRoundItem(R.mipmap.publish_add))
                .addItem(newItem(R.mipmap.ic_tab_strip_icon_category,R.mipmap.ic_tab_strip_icon_category_selected, "通知"))
                .addItem(newItem(R.mipmap.ic_tab_strip_icon_profile,R.mipmap.ic_tab_strip_icon_profile_selected, "我的"))
                .build()

        navigationController.addSimpleTabItemSelectedListener { index, old -> beforeOnclick(index, old) }
    }

    /**
     * 初始化Fragments
     */
    private fun initFragments() {
        //加载mFragments
        val firstFragment = findFragment(HomeFragment.newInstance().javaClass)
        if (firstFragment == null) {
            mFragments.add(homeFragment)
            mFragments.add(communityFragment)
            mFragments.add(notificationFragment)
            mFragments.add(mimeFragment)
            loadMultipleRootFragment(R.id.fl_tab_container, 0, mFragments[0], mFragments[1], mFragments[2], mFragments[3])
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            for (i in 0 until mFragments.size) {
                mFragments[i] = findFragment(mFragments[i].javaClass)
            }
        }
    }

    /**
     * 点击事件是否执行
     *
     * @param position 点击的下标
     * @return 是否可向下执行
     */
    fun beforeOnclick(position: Int, toDoHidden: Int) {
        //这是方法是显示隐藏调用，如果你不不需要进行判断就不用处理这个方法，如果你的app切换需要状态判断
        //才能确定是否允许切换就需要在这个方法写文章了举个例子，下面的例子是判断登录正常调整，不登录跳转登录页
        //if ((position == 3 || position == 4) && !isLogin) {
        //    LoginActivity.Start(_mActivity);
        //    mTabLayout.setCurrentTab(toDoHidden);
        //    return;
        // } else {
        //    showHideFragment(mFragments[position], mFragments[toDoHidden])
        //}
        showHideFragment(mFragments[position], mFragments[toDoHidden])
    }

    /**
     * start other BrotherFragment
     */
    fun startBrotherFragment(targetFragment: SupportFragment) {
        start(targetFragment)
    }

    /**
     * 正常tab
     */
    private fun newItem(drawable: Int, drawableCheck: Int, text: String): BaseTabItem {
        val mainTab = SpecialTab( requireActivity())
        mainTab.initialize(drawable, drawableCheck, text)
        mainTab.setTextDefaultColor(DisplayUtils.getColor( requireActivity(), R.color.tv_hint))
        mainTab.setTextCheckedColor(DisplayUtils.getColor( requireActivity(), R.color.colorBlack))
        return mainTab
    }

    /**
     * 圆形tab
     */
    private fun newRoundItem(drawable: Int): BaseTabItem {
        val mainTab = SpecialTabRound(requireActivity())
        mainTab.initialize(drawable)
        return mainTab
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}