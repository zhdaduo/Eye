package com.mor.eye.view.home

import android.content.Context
import android.view.KeyEvent
import android.view.View
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.other.startKActivity
import com.mor.eye.view.base.BaseActivity
import com.mor.eye.view.home.mime.MimeFragment
import com.mor.eye.view.home.newcommunity.NewCommunityFragment
import com.mor.eye.view.home.notification.NotificationFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), View.OnClickListener {
    private var homeFragment: HomeFragment? = null
    private var communityFragment: NewCommunityFragment? = null
    private var notificationFragment: NotificationFragment? = null
    private var mimeFragment: MimeFragment? = null

    override fun getLayout(): Int = R.layout.activity_home

    override fun init() {
        initTabDrawable()

        initFragment()

        initBottomTab()

        initEvent()
    }

    private fun initTabDrawable() {
        val homeDrawable = DisplayUtils.getDrawable(this, R.drawable.tab_home_bg)
        val focusDrawable = DisplayUtils.getDrawable(this, R.drawable.tab_focus_bg)
        val notifyDrawable = DisplayUtils.getDrawable(this, R.drawable.tab_notify_bg)
        val mineDrawable = DisplayUtils.getDrawable(this, R.drawable.tab_mine_bg)
        homeDrawable.setBounds(0, 0, 130, 80)
        focusDrawable.setBounds(0, 0, 130, 80)
        notifyDrawable.setBounds(0, 0, 130, 80)
        mineDrawable.setBounds(0, 0, 130, 80)
        tabHomePage.setCompoundDrawables(null, homeDrawable, null, null)
        tabFocus.setCompoundDrawables(null, focusDrawable, null, null)
        tabNotify.setCompoundDrawables(null, notifyDrawable, null, null)
        tabMine.setCompoundDrawables(null, mineDrawable, null, null)
    }

    private fun initEvent() {
        tabHomePage.setOnClickListener(this)
        tabFocus.setOnClickListener(this)
        tabNotify.setOnClickListener(this)
        tabMine.setOnClickListener(this)
    }

    private fun initFragment() {
        homeFragment = HomeFragment()
        communityFragment = NewCommunityFragment()
        notificationFragment = NotificationFragment()
        mimeFragment = MimeFragment()

        with(supportFragmentManager.beginTransaction()) {
            add(R.id.frame_container, homeFragment)
            add(R.id.frame_container, communityFragment)
            add(R.id.frame_container, notificationFragment)
            add(R.id.frame_container, mimeFragment)
            commit()
        }

        supportFragmentManager.beginTransaction()
                .show(homeFragment)
                .hide(communityFragment)
                .hide(notificationFragment)
                .hide(mimeFragment)
                .commit()
    }

    private fun initBottomTab() {
        tabHomePage.isChecked = true // 默认首页
        tabHomePage.setTextColor(DisplayUtils.getColor(this, R.color.main_text_color))
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.tabHomePage -> {
                clearState()
                tabHomePage.setTextColor(DisplayUtils.getColor(this, R.color.main_text_color))
                supportFragmentManager.beginTransaction()
                        .show(homeFragment)
                        .hide(communityFragment)
                        .hide(notificationFragment)
                        .hide(mimeFragment)
                        .commit()
            }

            R.id.tabFocus -> {
                clearState()
                tabFocus.setTextColor(DisplayUtils.getColor(this, R.color.main_text_color))
                supportFragmentManager.beginTransaction()
                        .show(communityFragment)
                        .hide(homeFragment)
                        .hide(notificationFragment)
                        .hide(mimeFragment)
                        .commit()
            }

            R.id.tabNotify -> {
                clearState()
                tabNotify.setTextColor(DisplayUtils.getColor(this, R.color.main_text_color))
                supportFragmentManager.beginTransaction()
                        .show(notificationFragment)
                        .hide(homeFragment)
                        .hide(communityFragment)
                        .hide(mimeFragment)
                        .commit()
            }
            R.id.tabMine -> {
                clearState()
                tabMine.setTextColor(DisplayUtils.getColor(this, R.color.main_text_color))
                supportFragmentManager.beginTransaction()
                        .show(mimeFragment)
                        .hide(homeFragment)
                        .hide(communityFragment)
                        .hide(notificationFragment)
                        .commit()
            }
        }
    }

    private fun clearState() {
        val unselectedColor = DisplayUtils.getColor(this, R.color.main_text_color)
        tabHomePage.setTextColor(unselectedColor)
        tabNotify.setTextColor(unselectedColor)
        tabFocus.setTextColor(unselectedColor)
        tabMine.setTextColor(unselectedColor)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return onExitActivity(keyCode, event)
    }

    companion object {
        fun open(context: Context) = context.startKActivity(HomeActivity::class) {}
    }
}
