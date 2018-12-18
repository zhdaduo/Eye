package com.mor.eye.ui.video

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mor.eye.util.NetworkUtils

import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer

/**
 * 计算滑动，自动播放的帮助类
 * Created by guoshuyu on 2017/11/2.
 */

class ScrollCalculatorHelper(private val playId: Int, private val rangeTop: Int, private val rangeBottom: Int) {

    private var firstVisible = 0
    private var lastVisible = 0
    private var visibleCount = 0
    private var runnable: PlayRunnable? = null

    private val playHandler = Handler()

    fun onScrollStateChanged(view: RecyclerView, scrollState: Int) {
        when (scrollState) {
            RecyclerView.SCROLL_STATE_IDLE -> playVideo(view)
        }
    }

    fun onScroll(view: RecyclerView, firstVisibleItem: Int, lastVisibleItem: Int, visibleItemCount: Int) {
        if (firstVisible == firstVisibleItem) {
            return
        }
        firstVisible = firstVisibleItem
        lastVisible = lastVisibleItem
        visibleCount = visibleItemCount
    }

    internal fun playVideo(view: RecyclerView?) {

        if (view == null) {
            return
        }

        val layoutManager = view.layoutManager

        var gsyBaseVideoPlayer: GSYBaseVideoPlayer? = null

        var needPlay = false

        for (i in 0 until visibleCount) {
            if (layoutManager.getChildAt(i) != null && layoutManager.getChildAt(i).findViewById<View>(playId) != null) {
                val player = layoutManager.getChildAt(i).findViewById<View>(playId) as GSYBaseVideoPlayer
                val rect = Rect()
                player.getLocalVisibleRect(rect)
                val height = player.height
                // 说明第一个完全可视
                if (rect.top == 0 && rect.bottom == height) {
                    gsyBaseVideoPlayer = player
                    if (player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_NORMAL || player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_ERROR) {
                        needPlay = true
                    }
                    break
                }
            }
        }

        if (gsyBaseVideoPlayer != null && needPlay) {
            if (runnable != null) {
                val tmpPlayer = runnable!!.gsyBaseVideoPlayer
                playHandler.removeCallbacks(runnable)
                runnable = null
                if (tmpPlayer === gsyBaseVideoPlayer) {
                    return
                }
            }
            runnable = PlayRunnable(gsyBaseVideoPlayer)
            // 降低频率
            playHandler.postDelayed(runnable, 400)
        }
    }

    private inner class PlayRunnable(internal var gsyBaseVideoPlayer: GSYBaseVideoPlayer?) : Runnable {

        override fun run() {
            var inPosition = false
            // 如果未播放，需要播放
            gsyBaseVideoPlayer?.let {
                val screenPosition = IntArray(2)
                gsyBaseVideoPlayer!!.getLocationOnScreen(screenPosition)
                val halfHeight = gsyBaseVideoPlayer!!.height / 2
                val rangePosition = screenPosition[1] + halfHeight
                // 中心点在播放区域内
                if (rangePosition in rangeTop..rangeBottom) {
                    inPosition = true
                }
                if (inPosition) {
                    startPlayLogic(gsyBaseVideoPlayer!!, gsyBaseVideoPlayer!!.context)
                }
            }
        }
    }

    /***************************************自动播放的点击播放确认 */
    private fun startPlayLogic(gsyBaseVideoPlayer: GSYBaseVideoPlayer, context: Context) {
        if (!NetworkUtils.isWifiConnected(context)) {
            return
        }
        gsyBaseVideoPlayer.startPlayLogic()
    }
}
