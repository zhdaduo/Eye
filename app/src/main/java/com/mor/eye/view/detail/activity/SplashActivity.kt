package com.mor.eye.view.detail.activity

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.mor.eye.R
import com.mor.eye.databinding.ActivitySplashBinding
import com.mor.eye.util.glide.loadPlaceWithColor
import com.mor.eye.view.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var mSplashBinding: ActivitySplashBinding
    private var animator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mSplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        initBackground()
        startAnimation()
    }

    private fun initBackground() {
        iv_bg.loadPlaceWithColor(this@SplashActivity, "http://img.kaiyanapp.com/824badb5c9fb9e272c2ea6fb7dba01cd.jpeg?imageMogr2/quality/60/format/jpg", R.color.primary_dark_material_dark)
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofObject(FloatEvaluator(), 1.0f, 1.2f)
        animator?.duration = 0
        animator?.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val value = animation.animatedValue as Float
                if (value != 1.2f) {
                    iv_bg.scaleX = value
                    iv_bg.scaleY = value
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

    override fun onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
        super.onBackPressed()
    }
}
