package com.mor.eye.view.detail.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.mor.eye.R
import com.mor.eye.util.DisplayManager
import com.mor.eye.util.glide.loadImageBlur
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.shuyu.gsyvideoplayer.GSYVideoManager
import org.koin.android.viewmodel.ext.android.viewModel
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.gyf.barlibrary.ImmersionBar
import com.mor.eye.ui.video.EyepetizerVideoPlayer
import com.mor.eye.util.other.bindable
import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.gyf.barlibrary.BarHide
import com.mor.eye.ui.AudioServiceActivityLeak
import com.mor.eye.ui.video.VideoSampleListener
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.NetworkUtils
import com.mor.eye.util.other.DataSourceConstant.Companion.VIDEO_BLUR_URL
import com.mor.eye.util.other.DataSourceConstant.Companion.VIDEO_FEED_URL
import com.mor.eye.util.other.DataSourceConstant.Companion.VIDEO_ID
import com.mor.eye.util.other.DataSourceConstant.Companion.VIDEO_PLAY_URL
import com.mor.eye.util.other.DataSourceConstant.Companion.VIDEO_TITLE
import com.mor.eye.util.other.showToast
import com.mor.eye.view.base.BaseSupportActivity
import com.shuyu.gsyvideoplayer.utils.Debuger
import me.yokeyword.fragmentation.anim.FragmentAnimator

class VideoDetailActivity : BaseSupportActivity() {

    private val model: VideoDetailViewModel by viewModel()

    private val videoId by unsafeLazy { intent.getIntExtra(VIDEO_ID, 0) }
    private val videoFeedUrl by unsafeLazy { intent.getStringExtra(VIDEO_FEED_URL) }
    private val videoPlayUrl by unsafeLazy { intent.getStringExtra(VIDEO_PLAY_URL) }
    private val videoBlurUrl by unsafeLazy { intent.getStringExtra(VIDEO_BLUR_URL) }
    private val videoTitle by unsafeLazy { intent.getStringExtra(VIDEO_TITLE) }
    private val controller by unsafeLazy { VideoDetailController(this@VideoDetailActivity, callbacks) }
    private val mImmersionBar by unsafeLazy { ImmersionBar.with(this) }

    private val videoPlayer: EyepetizerVideoPlayer by bindable(R.id.video_player)
    private val rvVideoDetail: EpoxyRecyclerView by bindable(R.id.video_detail_rv)
    private val ivBackground: ImageView by bindable(R.id.iv_background)

    /**
     * 全屏工具类
     */
    private val orientationUtils: OrientationUtils by unsafeLazy { OrientationUtils(this, videoPlayer) }
    /**
     * 视频宽度
     */
    private var videoWidth: Int = 0
    /**
     * 视频高度
     */
    private var videoheight: Int = 0
    /**
     * 从页面返回
     */
    private var isRestart: Boolean = false


    override fun getContentViewResId(): Int = R.layout.activity_video_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showVideo(videoFeedUrl, videoPlayUrl, videoBlurUrl, videoTitle)
        initVideo()
        initRecyclerView()
        observeViewModel()
        model.id = videoId.toString()
        model.getRecommend()
        model.getVideoDetail()
        controller.setVideoCallback(object: VideoDetailController.OnVideoCallback {
            override fun likeCallback() {
                showToast("喜爱")
            }

            override fun shareCallback() {
                showToast("分享")
            }

            override fun replyCallback() {
                showToast("评论")
            }

            override fun downloadCallback() {
                showToast("缓存")
            }

            override fun focusCallback() {
                showToast("关注")
            }
        })
    }

    override fun onNewIntent(intent: Intent) {
        val videoFeedUrl = intent.getStringExtra(VIDEO_FEED_URL)
        val videoPlayUrl = intent.getStringExtra(VIDEO_PLAY_URL)
        val videoBlurUrl = intent.getStringExtra(VIDEO_BLUR_URL)
        val videoTitle = intent.getStringExtra(VIDEO_TITLE)
        showVideo(videoFeedUrl, videoPlayUrl, videoBlurUrl, videoTitle)
        initVideo()
        initRecyclerView()
        observeViewModel()
        model.id = videoId.toString()
        model.getRecommend()
        model.getVideoDetail()
    }

    override fun initImmersion() {
        mImmersionBar
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init()
    }

    private fun observeViewModel() {
        model.uiLoadData.observeK(this) {
            controller.updateCom(it!!)
        }
        model.uiVideoData.observeK(this) {
            controller.update(it!!)
        }
    }

    /**
     * 视频播放器
     */
    private fun initVideo() {
        videoPlayer.visibility = View.VISIBLE
        videoWidth = DisplayUtils.getScreenWidth(this)
        videoheight = ((videoWidth * 210f / 375f).toInt())
        videoPlayer.layoutParams.width = videoWidth
        videoPlayer.layoutParams.height = videoheight

        videoPlayer.titleTextView.visibility = View.GONE
        //播放器自带返回键
        videoPlayer.backButton.visibility = View.VISIBLE
        videoPlayer.backButton.setOnClickListener { onBackPressed() }
        //设置旋转
        orientationUtils.isEnable = false

        videoPlayer.setIsTouchWiget(true)
        videoPlayer.isRotateViewAuto = false
        videoPlayer.isLockLand = false
        videoPlayer.isShowFullAnimation = false
        videoPlayer.setmOpenPreView(true)
        videoPlayer.dismissControlTime = 2000

        //如果是4G网络
        if (NetworkUtils.is4G(this)) {
            videoPlayer.show4gPlay()
        }

        //设置全屏按键功能
        videoPlayer.fullscreenButton.setOnClickListener {
            val currentVideoWidth = GSYVideoManager.instance().currentVideoWidth
            val currentVideoHeight = GSYVideoManager.instance().currentVideoHeight
            val rotate = videoPlayer.rotate
            if (rotate == 0 && currentVideoWidth > currentVideoHeight) {
                orientationUtils.resolveByClick()
            }
            videoPlayer.startWindowFullscreen(this@VideoDetailActivity, true, true)
            videoPlayer.titleTextView.visibility = View.VISIBLE
        }
        videoPlayer.setOnPlayClickListener {
            if (!NetworkUtils.isNetworkConnected(this)) {
                showToast("无可使用网络")
            }
        }
        videoPlayer.setVideoAllCallBack(object : VideoSampleListener() {
            override fun onQuitFullscreen(url: String, vararg objects: Any) {
                super.onQuitFullscreen(url, objects)
                orientationUtils.backToProtVideo()
                videoPlayer.titleTextView.visibility = View.GONE
            }

            override fun onAutoComplete(url: String, vararg objects: Any) {
                super.onAutoComplete(url, objects)
                orientationUtils.backToProtVideo()
                GSYVideoManager.backFromWindowFull(this@VideoDetailActivity)
            }

            override fun onPlayError(url: String, vararg objects: Any) {
                super.onPlayError(url, objects)
                showToast("视频出错或不存在")
            }
        })
    }

    private fun initRecyclerView() {
        rvVideoDetail.apply {
            itemAnimator = null
            adapter = controller.adapter
        }
    }

    private fun showVideo(feedUrl: String, playUrl: String, blurUrl: String, title: String) {
        Debuger.printfLog("GSYVideoPlayer - - - - loadThumbImage")
        videoPlayer.loadThumbImage(feedUrl)
        val backgroundUrl = blurUrl + "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
        ivBackground.loadImageBlur(this, backgroundUrl)
        //videoPlayer.loadThumbImage(coverUrl)
        // 断网自动重新连接
        val url = "ijkhttphook:$playUrl"
        videoPlayer.setUp(url, true, title)
        if (NetworkUtils.isWifiConnected(this)) {
            videoPlayer.startPlayLogic();
        }
    }

    override fun onBackPressedSupport() {
        Debuger.printfLog("GSYVideoPlayer - - - - onBackPressed")
        orientationUtils.backToProtVideo();

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressedSupport()
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return FragmentAnimator(R.anim.screen_top_in ,R.anim.screen_null ,R.anim.screen_null, R.anim.screen_bottom_out)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(AudioServiceActivityLeak.preventLeakOf(base))
    }

    override fun onRestart() {
        Debuger.printfLog("GSYVideoPlayer - - - - onRestart")
        super.onRestart()
        isRestart = true
    }

    override fun onPause() {
        Debuger.printfLog("GSYVideoPlayer - - - - onPause")
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onResume() {
        Debuger.printfLog("GSYVideoPlayer - - - - onResume")
        super.onResume()
        initImmersion()
        if (isRestart) {
            videoPlayer.onVideoResume(true)
        }
    }

    override fun onDestroy() {
        Debuger.printfLog("GSYVideoPlayer - - - - onDestroy")
        orientationUtils.releaseListener();
        mImmersionBar.destroy()
        videoPlayer.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
        rvVideoDetail.adapter = null
        super.onDestroy()
    }

    companion object {

        fun open(context: Context, videoId: Int, feedUrl: String, playUrl: String, blurUrl: String, videoTitle: String) = context.startKActivity(VideoDetailActivity::class) {
            putExtra(VIDEO_ID, videoId)
            putExtra(VIDEO_FEED_URL, feedUrl)
            putExtra(VIDEO_PLAY_URL, playUrl)
            putExtra(VIDEO_BLUR_URL, blurUrl)
            putExtra(VIDEO_TITLE, videoTitle)
        }
    }
}
