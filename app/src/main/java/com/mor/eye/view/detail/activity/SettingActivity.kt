package com.mor.eye.view.detail.activity

import android.content.Context
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.mor.eye.R
import com.mor.eye.actionText
import com.mor.eye.subscriptText
import com.mor.eye.util.epoxy.withModels
import com.mor.eye.util.other.show
import com.mor.eye.util.other.showToast
import com.mor.eye.util.other.startKActivity
import com.mor.eye.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_setting

    override fun init() {
        initSetToolbar()
        initRecyclerView()
    }

    private fun initSetToolbar() {
        initToolbar()
        tvBoldTitle.show()
        tvBoldTitle.text = getString(R.string.setting)
    }

    private fun initRecyclerView() {
        category_rv.addItemDecoration(EpoxyItemSpacingDecorator(30))
        category_rv.withModels {

            switchText(this@SettingActivity) {
                id(1)
                settingTitle(String.format(getString(R.string.setting_format), getString(R.string.daily_update)))
                openClick { _ -> showToast(getString(R.string.setting_open)) }
                closeClick { _ -> showToast(getString(R.string.setting_close)) }
            }
            switchText(this@SettingActivity) {
                id(2)
                settingTitle(String.format(getString(R.string.setting_format), getString(R.string.wifi_auto_play)))
                openClick { _ -> showToast(getString(R.string.setting_open)) }
                closeClick { _ -> showToast(getString(R.string.setting_close)) }
            }
            switchText(this@SettingActivity) {
                id(3)
                settingTitle(String.format(getString(R.string.setting_format), getString(R.string.translate)))
                openClick { _ -> showToast(getString(R.string.setting_open)) }
                closeClick { _ -> showToast(getString(R.string.setting_close)) }
            }
            actionText {
                id(4)
                actionText(getString(R.string.clear_cache))
                actionClick { _ -> showToast(getString(R.string.clear_cache)) }
            }
            actionText {
                id(5)
                actionText(getString(R.string.cache_dir))
                actionClick { _ -> showToast(getString(R.string.cache_dir)) }
            }
            actionText {
                id(6)
                actionText(getString(R.string.update))
                actionClick { _ -> showToast(getString(R.string.update)) }
            }
            actionText {
                id(7)
                actionText(getString(R.string.ua))
                actionClick { _ -> showToast(getString(R.string.ua)) }
            }
            actionText {
                id(8)
                actionText(getString(R.string.video_stament))
                actionClick { _ -> showToast(getString(R.string.video_stament)) }
            }
            actionText {
                id(9)
                actionText(getString(R.string.report))
                actionClick { _ -> showToast(getString(R.string.report)) }
            }
            subscriptText {
                id(10)
            }
        }
    }

    companion object {
        fun open(context: Context) = context.startKActivity(SettingActivity::class) {}
    }
}
