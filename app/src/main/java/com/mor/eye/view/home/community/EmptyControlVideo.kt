package com.mor.eye.view.home.community

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.Surface
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mor.eye.R
import com.mor.eye.util.other.unsafeLazy
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

/**
 * 带封面不带控制
 * Created by guoshuyu on 2017/9/3.
 */
class EmptyControlVideo @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : StandardGSYVideoPlayer(context, attrs) {

    private val mCoverImage by unsafeLazy { findViewById<ImageView>(R.id.thumbImage) }
    private var mCoverOriginUrl: String? = null

    private var mDefaultRes: Int = 0

    override fun init(context: Context) {
        super.init(context)

        if (mThumbImageViewLayout != null && (mCurrentState == -1 || mCurrentState == GSYVideoView.CURRENT_STATE_NORMAL || mCurrentState == GSYVideoView.CURRENT_STATE_ERROR)) {
            mThumbImageViewLayout.visibility = View.VISIBLE
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_video_empty_control
    }

    private fun loadCoverImage(url: String, res: Int) {
        mCoverOriginUrl = url
        mDefaultRes = res
        Glide.with(context.applicationContext)
                .setDefaultRequestOptions(
                        RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .error(res)
                                .placeholder(res))
                .load(url)
                .into(mCoverImage)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        GSYVideoManager.releaseAllVideos()
    }

    override fun startWindowFullscreen(context: Context, actionBar: Boolean, statusBar: Boolean): GSYBaseVideoPlayer {
        val gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar)
        mCoverOriginUrl?.let { loadCoverImage(it, mDefaultRes) }
        return gsyBaseVideoPlayer
    }

    override fun showSmallVideo(size: Point, actionBar: Boolean, statusBar: Boolean): GSYBaseVideoPlayer {
        val sampleCoverVideo = super.showSmallVideo(size, actionBar, statusBar) as SampleCoverVideo
        mStartButton.visibility = View.GONE
        mStartButton = null
        return sampleCoverVideo
    }

    override fun onSurfaceUpdated(surface: Surface?) {
        super.onSurfaceUpdated(surface)
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == View.VISIBLE) {
            mThumbImageViewLayout.visibility = View.INVISIBLE
        }
    }

    override fun setViewShowState(view: View?, visibility: Int) {
        if (view === mThumbImageViewLayout && visibility != View.VISIBLE) {
            return
        }
        super.setViewShowState(view, visibility)
    }

    override fun touchDoubleUp() {
        // super.touchDoubleUp();
    }
}