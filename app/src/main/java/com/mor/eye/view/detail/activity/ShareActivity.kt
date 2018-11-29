package com.mor.eye.view.detail.activity

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mor.eye.R
import com.mor.eye.repository.data.ShareBean
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.glide.loadImage
import com.mor.eye.util.glide.loadPlaceWithColor
import com.mor.eye.util.other.ShareArgumentConstant.Companion.SHARE_BEAN
import com.mor.eye.util.other.show
import com.mor.eye.util.other.showToast
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_share.*

class ShareActivity : BaseActivity(), View.OnClickListener {
    private val shareBean by unsafeLazy { intent.getParcelableExtra(SHARE_BEAN) as ShareBean }
    private var hideAnimation: Animation? = null
    private var showAnimation: Animation? = null
    override fun getLayout(): Int = R.layout.activity_share

    override fun init() {
        iv_share_cover.loadPlaceWithColor(this, shareBean.blurUrl, DisplayUtils.getColor(this, R.color.detail_bg1))
        iv_cover.loadImage(this, shareBean.coverUrl)
        tv_title.text = shareBean.title
        showBottom()
    }

    private fun hideBottom() {
        hideAnimation = AnimationUtils.loadAnimation(
                this, R.anim.layout_bottom_hide)
        hideAnimation?.fillAfter = true
        layout_bottom.startAnimation(hideAnimation)
    }

    private fun showBottom() {
        layout_bottom.show()
        showAnimation = AnimationUtils.loadAnimation(
                this, R.anim.layout_bottom_show)
        showAnimation?.fillAfter = true
        layout_bottom.startAnimation(showAnimation)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_share_vx -> {
                showToast(getString(R.string.isWorking))
            }
            R.id.tv_share_vx_friend -> {
                showToast(getString(R.string.isWorking))
            }
            R.id.tv_share_qq -> {
                showToast(getString(R.string.isWorking))
            }
            R.id.tv_share_wb -> {
                showToast(getString(R.string.isWorking))
            }
            R.id.tv_cancel -> {
                hideBottom()
            }
        }
    }

    override fun onBackPressed() {
        hideBottom()
        hideAnimation?.cancel()
        showAnimation?.cancel()
        hideAnimation = null
        showAnimation = null
        super.onBackPressed()
        overridePendingTransition(R.anim.screen_null, R.anim.screen_top_out)
    }

    companion object {
        fun open(context: Context, shareBean: ShareBean) = context.startKActivity(ShareActivity::class) {
            putExtra(SHARE_BEAN, shareBean)
        }
    }
}
