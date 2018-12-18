package com.mor.eye.view.base

import android.os.Bundle
import com.mor.eye.R
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultNoAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

abstract class BaseActivity : BaseSupportActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRootFragment(R.id.fl_container, setFragment());
    }

    override fun getContentViewResId(): Int {
        return R.layout.ac_base
    }

    /**
     * 设置整个架构的第一个fragment
     *
     * @return
     */
    abstract fun setFragment(): SupportFragment

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
        //        return super.onCreateFragmentAnimator();
        //无动画
        return DefaultNoAnimator()
        // 设置横向(和安卓4.x动画相同)
        //        return new DefaultHorizontalAnimator();
        // 设置自定义动画
        //        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }

    override fun showToolBar(): Boolean {
        return super.showToolBar()
    }
}