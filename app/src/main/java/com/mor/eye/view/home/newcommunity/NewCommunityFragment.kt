package com.mor.eye.view.home.newcommunity

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mor.eye.R
import com.mor.eye.util.databinding.convertStringToFace
import com.mor.eye.view.base.BaseFragment

class NewCommunityFragment : BaseFragment() {
    private lateinit var rvCommunity: EpoxyRecyclerView

    override fun getLayoutRes(): Int = R.layout.fragment_new_community

    override fun initView(rootView: View) {
        rvCommunity = rootView.findViewById(R.id.community_rv)

        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        tv_title?.visibility = View.VISIBLE
        tv_title?.typeface = convertStringToFace(getString(R.string.lobster))
        tv_title?.textSize = 23f
        tv_title?.setText(R.string.community)
    }

    override fun initLogic() {

    }

    override fun showToolBar(): Boolean {
        return true
    }

    companion object {
        fun newInstance(): NewCommunityFragment {
            return NewCommunityFragment()
        }
    }
}