package com.mor.eye.view.detail.activity

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.mor.eye.R
import com.mor.eye.util.glide.loadImage
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.DataSourceConstant.Companion.VIDEO_DATA
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.BaseActivity
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import kotlinx.android.synthetic.main.activity_video_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class VideoDetailActivity : BaseActivity() {
    private val model: VideoDetailViewModel by viewModel()
    private val controller by unsafeLazy { VideoDetailController(this@VideoDetailActivity, callbacks) }
    private val imageView by unsafeLazy { ImageView(this@VideoDetailActivity) }
    private val gsyVideoOptionBuilder: GSYVideoOptionBuilder = GSYVideoOptionBuilder()
    private val videoId by unsafeLazy { intent.getIntExtra(VIDEO_DATA, 0) }

    override fun getLayout(): Int = R.layout.activity_video_detail

    override fun init() {
        initRecyclerView()
        observeViewModel()
        model.id = videoId.toString()
        model.getRecommend()
        model.getVideoDetail()
    }

    private fun observeViewModel() {
        model.uiLoadData.observeK(this) {
            controller.updateCom(it!!)
        }
        model.uiVideoData.observeK(this) {
            controller.update(it!!)
            initVideoPlayer(it.cover.feed, it.playUrl)
            iv_background.loadImage(this, it.cover.blurred)
        }
    }

    private fun initVideoPlayer(coverUrl: String, playUrl: String) {
        // 增加封面
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.loadImage(this@VideoDetailActivity, coverUrl)
        val gsyVideoPlayer = video_player
        if (imageView.parent != null) {
            val viewGroup = imageView.parent as ViewGroup
            viewGroup.removeView(imageView)
        }

        gsyVideoOptionBuilder.setIsTouchWiget(false)
                .setThumbImageView(imageView)
                .setUrl(playUrl)
                .setSetUpLazy(true) // lazy可以防止滑动卡顿
                .setCacheWithPlay(true)
                .setRotateViewAuto(false)
                .setLockLand(true)
                .setPlayTag(TAG) // 防止错位设置
                .setIsTouchWiget(true) // 是否可以滑动调整
                .setAutoFullWithSize(false) // 是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
                .setShowFullAnimation(true) // 全屏动画
                .setNeedLockFull(true)
                .setLooping(true)
                .build(gsyVideoPlayer)

        // 设置返回键
        gsyVideoPlayer.backButton.setOnClickListener {
            onBackPressed()
        }
        // 设置全屏按键
        gsyVideoPlayer.fullscreenButton.setOnClickListener {
            // 第一个true是否需要隐藏actionbar，第二个true是否需要隐藏 statusbar
            gsyVideoPlayer.startWindowFullscreen(this, true, true)
        }
        gsyVideoPlayer.startPlayLogic()
    }

    private fun initRecyclerView() {
        video_detail_rv.apply {
            itemAnimator = null
            adapter = controller.adapter
        }
    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
        overridePendingTransition(R.anim.screen_null, R.anim.screen_bottom_out)
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    private fun release() {
        video_detail_rv.adapter = null
        GSYVideoManager.releaseAllVideos()
    }

    companion object {
        const val TAG = "VideoDetailActivity"

        fun open(context: Context, videoId: Int) = context.startKActivity(VideoDetailActivity::class) {
            putExtra(VIDEO_DATA, videoId)
        }
    }
}
