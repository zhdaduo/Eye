package com.mor.eye.view.detail.activity

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import com.mor.eye.R
import com.mor.eye.util.glide.loadPlaceWithColor
import com.mor.eye.util.other.bindable
import com.mor.eye.view.base.BaseSupportActivity
import com.mor.eye.view.home.HomeActivity

class SplashActivity : BaseSupportActivity() {
    private val ivBg: ImageView by bindable(R.id.iv_bg)

    private var animator: ValueAnimator? = null

    override fun getContentViewResId(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initBackground()
        startAnimation()
    }

    private fun initBackground() {
        ivBg.loadPlaceWithColor(this@SplashActivity, "http://img.kaiyanapp.com/824badb5c9fb9e272c2ea6fb7dba01cd.jpeg?imageMogr2/quality/60/format/jpg", R.color.primary_dark_material_dark)
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofObject(FloatEvaluator(), 1.0f, 1.2f)
        animator?.duration = 0
        animator?.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val value = animation.animatedValue as Float
                if (value != 1.2f) {
                    ivBg.scaleX = value
                    ivBg.scaleY = value
                } else {
                    goToActivity()
                }
            }

            private fun goToActivity() {
                HomeActivity.open(this@SplashActivity)
                overridePendingTransition(0, android.R.anim.fade_out)
                finish()
            }
        })
        animator?.start()
    }

    override fun onDestroy() {
        animator?.removeAllUpdateListeners()
        animator?.cancel()
        animator = null
        super.onDestroy()
    }

    override fun onBackPressedSupport() {
        appManager.AppExit(this)
        super.onBackPressedSupport()
    }
}
