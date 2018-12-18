package com.mor.eye.view.home

import android.content.Context
import android.support.v4.app.ActivityCompat
import com.mor.eye.R
import com.mor.eye.util.other.showToast
import com.mor.eye.util.other.startKActivity
import com.mor.eye.view.base.BaseActivity
import me.yokeyword.fragmentation.SupportFragment

class HomeActivity : BaseActivity() {

    override fun setFragment(): SupportFragment = MainFragment.newInstance()

    override fun getContentViewResId(): Int = R.layout.activity_home

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                ActivityCompat.finishAfterTransition(this)
            } else {
                TOUCH_TIME = System.currentTimeMillis()
                showToast(R.string.exit_program_hint)
            }
        }
    }

    companion object {
        // 再点一次退出程序时间设置
        private const val WAIT_TIME = 2000L
        private var TOUCH_TIME: Long = 0

        fun open(context: Context) = context.startKActivity(HomeActivity::class) {}
    }
}
