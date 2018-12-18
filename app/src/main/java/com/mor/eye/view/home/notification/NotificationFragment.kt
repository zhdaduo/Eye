package com.mor.eye.view.home.notification

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.mor.eye.R
import com.mor.eye.util.databinding.convertStringToFace
import com.mor.eye.view.base.BaseFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout

class NotificationFragment : BaseFragment() {
    private lateinit var viewToolbar: Toolbar
    private lateinit var tvToolbarTitle: TextView
    private lateinit var tabLayout: SmartTabLayout
    private lateinit var viewPager: ViewPager

    override fun getLayoutRes(): Int = R.layout.fragment_notification

    override fun initView(rootView: View) {
        viewToolbar = rootView.findViewById(R.id.toolbar)
        tvToolbarTitle = rootView.findViewById(R.id.toolbar_title)
        tabLayout = rootView.findViewById(R.id.tab_view_pager)
        viewPager = rootView.findViewById(R.id.view_pager)

        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(viewToolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        tvToolbarTitle.setText(R.string.notification)
        tvToolbarTitle.typeface = convertStringToFace(getString(R.string.lobster))
    }

    override fun initLogic() {

    }

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }
}