package com.mor.eye.ui

import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.View.OnTouchListener

/**
 *
 * @ClassName: ColorFilterImageView
 * @Description: 实现图像根据按下抬起动作变化颜色
 * @author huym
 */
class ColorFilterImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : android.support.v7.widget.AppCompatImageView(context, attrs, defStyle), OnTouchListener {

    init {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN  // 按下时图像变灰
            -> setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY)
            MotionEvent.ACTION_UP   // 手指离开或取消操作时恢复原色
                , MotionEvent.ACTION_CANCEL -> setColorFilter(Color.TRANSPARENT)
            else -> {
            }
        }
        return false
    }
}