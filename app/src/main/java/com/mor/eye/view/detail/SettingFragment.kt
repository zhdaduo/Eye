package com.mor.eye.view.detail

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mor.eye.R
import com.mor.eye.actionText
import com.mor.eye.subscriptText
import com.mor.eye.util.databinding.convertStringToFace
import com.mor.eye.util.epoxy.withModels
import com.mor.eye.view.base.BaseFragment
import com.mor.eye.view.epoxy.switchText

class SettingFragment: BaseFragment() {

    private var rvSetting: EpoxyRecyclerView? = null
    override fun getLayoutRes(): Int = R.layout.activity_setting

    override fun initView(rootView: View) {
        rvSetting = rootView.findViewById(R.id.setting_rv)

        setupToolbar()
        initRecyclerView()
    }

    override fun initLogic() {

    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.apply {
            title = null
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setNavigationIcon(R.drawable.ic_arrow_left_black)
        }
        tv_title?.apply {
            setText(R.string.setting)
            visibility = View.VISIBLE
            typeface = convertStringToFace(getString(R.string.lan_ting_bold))
        }
    }

    private fun initRecyclerView() {
        rvSetting?.addItemDecoration(EpoxyItemSpacingDecorator(30))
        rvSetting?.withModels {

            switchText(requireActivity()) {
                id(1)
                settingTitle(String.format(getString(R.string.setting_format), getString(R.string.daily_update)))
                openClick { _ -> toast(R.string.setting_open) }
                closeClick { _ -> toast(R.string.setting_close) }
            }
            switchText(requireActivity()) {
                id(2)
                settingTitle(String.format(getString(R.string.setting_format), getString(R.string.wifi_auto_play)))
                openClick { _ -> toast(R.string.setting_open) }
                closeClick { _ -> toast(R.string.setting_close) }
            }
            switchText(requireActivity()) {
                id(3)
                settingTitle(String.format(getString(R.string.setting_format), getString(R.string.translate)))
                openClick { _ -> toast(R.string.setting_open) }
                closeClick { _ -> toast(R.string.setting_close) }
            }
            actionText {
                id(4)
                actionText(getString(R.string.clear_cache))
                actionClick { _ -> toast(R.string.clear_cache) }
            }
            actionText {
                id(5)
                actionText(getString(R.string.cache_dir))
                actionClick { _ -> toast(R.string.cache_dir) }
            }
            actionText {
                id(6)
                actionText(getString(R.string.update))
                actionClick { _ -> toast(R.string.update) }
            }
            actionText {
                id(7)
                actionText(getString(R.string.ua))
                actionClick { _ -> toast(R.string.ua) }
            }
            actionText {
                id(8)
                actionText(getString(R.string.video_stament))
                actionClick { _ -> toast(R.string.video_stament) }
            }
            actionText {
                id(9)
                actionText(getString(R.string.report))
                actionClick { _ ->toast(R.string.report) }
            }
            subscriptText {
                id(10)
            }
        }
    }

    override fun showToolBar(): Boolean {
        return true
    }

    override fun onDestroyView() {
        rvSetting?.recycledViewPool?.clear()
        rvSetting?.adapter = null
        rvSetting = null
        toolbar?.setNavigationOnClickListener(null)
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }
}