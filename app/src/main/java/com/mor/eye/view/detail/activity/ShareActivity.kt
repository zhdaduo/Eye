package com.mor.eye.view.detail.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.mor.eye.R
import com.mor.eye.repository.data.ShareBean
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.glide.loadImage
import com.mor.eye.util.glide.loadPlaceWithColor
import com.mor.eye.util.other.*
import com.mor.eye.util.other.ShareArgumentConstant.Companion.SHARE_BEAN
import com.mor.eye.view.base.BaseSupportActivity
import me.yokeyword.fragmentation.anim.FragmentAnimator

class ShareActivity : BaseSupportActivity(), View.OnClickListener {
    private val ivShareCover: ImageView by bindable(R.id.iv_share_cover)
    private val ivCover: ImageView by bindable(R.id.iv_cover)
    private val tvTitle: TextView by bindable(R.id.tv_title)
    private val layoutBottom: RelativeLayout by bindable(R.id.layout_bottom)

    private val shareBean by unsafeLazy { intent.getParcelableExtra(SHARE_BEAN) as ShareBean }
    private var hideAnimation: Animation? = null
    private var showAnimation: Animation? = null

    override fun getContentViewResId(): Int = R.layout.activity_share

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ivShareCover.loadPlaceWithColor(this, shareBean.blurUrl, DisplayUtils.getColor(this, R.color.detail_bg1))
        ivCover.loadImage(this, shareBean.coverUrl)
        tvTitle.text = shareBean.title
        showBottom()
    }

    private fun hideBottom() {
        hideAnimation = AnimationUtils.loadAnimation(
                this, R.anim.layout_bottom_hide)
        hideAnimation?.fillAfter = true
        layoutBottom.startAnimation(hideAnimation)
    }

    private fun showBottom() {
        layoutBottom.show()
        showAnimation = AnimationUtils.loadAnimation(
                this, R.anim.layout_bottom_show)
        showAnimation?.fillAfter = true
        layoutBottom.startAnimation(showAnimation)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_share_vx -> { showToast(getString(R.string.isWorking)) }
            R.id.tv_share_vx_friend -> { showToast(getString(R.string.isWorking)) }
            R.id.tv_share_qq -> { showToast(getString(R.string.isWorking)) }
            R.id.tv_share_wb -> { showToast(getString(R.string.isWorking)) }
            R.id.tv_cancel -> { hideBottom() }
        }
    }

    override fun onBackPressedSupport() {
        hideBottom()
        hideAnimation?.cancel()
        showAnimation?.cancel()
        hideAnimation = null
        showAnimation = null
        super.onBackPressedSupport()
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return FragmentAnimator(R.anim.screen_top_in ,R.anim.screen_null ,R.anim.screen_null, R.anim.screen_top_out)
    }

    companion object {
        fun open(context: Context, shareBean: ShareBean) = context.startKActivity(ShareActivity::class) {
            putExtra(SHARE_BEAN, shareBean)
        }
    }
}
