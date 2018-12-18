package com.mor.eye.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.MotionEvent
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils

/**
 * Created by darryrzhong on 2018/9/10.
 */

class SearchEditText : AppCompatEditText {

    /**
     * 定义左侧搜索图标 & 一键删除图标
     */
    private var clearDrawable: Drawable? = null
    private var searchDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    /**
     * 初始化 图标资源
     */
    private fun init() {
        clearDrawable = DisplayUtils.getDrawable(context, R.drawable.ic_action_delete)
        searchDrawable = DisplayUtils.getDrawable(context, R.mipmap.ic_action_search)
        setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00))
        // 设置左边搜索图标
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null)
    }

    /**
     * 通过监听复写EditText本身的方法来确定是否显示删除图标
     * 监听方法：onTextChanged（） & onFocusChanged（）
     * 调用时刻：当输入框内容变化时 & 焦点发生变化时
     */
    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        setClearIconVisible(hasFocus() && text.isNotEmpty())
        // hasFocus()返回是否获得EditTEXT的焦点，即是否选中
        // setClearIconVisible（） = 根据传入的是否选中 & 是否有输入来判断是否显示删除图标
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        setClearIconVisible(focused && length() > 0) // focused = 是否获得焦点
    }

    /**
     * 判断是否显示删除图标
     */
    private fun setClearIconVisible(visible: Boolean) {
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null,
                if (visible) clearDrawable else null, null)
    }

    /**
     * 对删除图标区域设置点击事件，即"点击 = 清空搜索框内容"
     * 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
        // 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
            MotionEvent.ACTION_UP -> {
                val drawable = clearDrawable
                if (drawable != null && event.x <= width - paddingRight &&
                        event.x >= width - paddingRight - drawable.bounds.width()) {
                    setText("")
                    onDrawableClearListener?.onDrawableClearClick()
                }

                // 判断DrawableLeft是否被点击
                val drawableLeft = compoundDrawables[0]
                // 当按下的位置 < 在EditText的到左边间距+图标的宽度+Padding
                if (drawableLeft != null && event.rawX <= left + totalPaddingLeft + drawableLeft.bounds.width()) {
                    // 执行DrawableLeft点击事件
                    onDrawableLeftListener?.onDrawableLeftClick()
                }

                // 监听DrawableRight
                val drawableRight = compoundDrawables[2]
                // 当按下的位置 > 在EditText的到右边间距-图标的宽度-Padding
                if (drawableRight != null && event.rawX >= right - totalPaddingRight - drawableRight.bounds.width()) {
                    // 执行DrawableRight点击事件
                    onDrawableRightListener?.onDrawableRightClick()
                }
            }
        }
        // event.getX() ：抬起时的位置坐标
        // getWidth()：控件的宽度
        // getPaddingRight():删除图标图标右边缘至EditText控件右边缘的距离
        // 即：getWidth() - getPaddingRight() = 删除图标的右边缘坐标 = X1
        // getWidth() - getPaddingRight() - drawable.getBounds().width() = 删除图标左边缘的坐标 = X2
        // 所以X1与X2之间的区域 = 删除图标的区域
        // 当手指抬起的位置在删除图标的区域（X2=<event.getX() <=X1），即视为点击了删除图标 = 清空搜索框内容
        return super.onTouchEvent(event)
    }

    // 定义一个DrawableLeft点击事件接口
    interface OnDrawableLeftListener {
        fun onDrawableLeftClick()
    }

    private var onDrawableLeftListener: OnDrawableLeftListener? = null

    fun setOnDrawableLeftListener(onDrawableLeftListener: OnDrawableLeftListener) {
        this.onDrawableLeftListener = onDrawableLeftListener
    }

    // 定义一个DrawableRight点击事件接口
    interface OnDrawableRightListener {
        fun onDrawableRightClick()
    }

    private var onDrawableRightListener: OnDrawableRightListener? = null

    fun setOnDrawableRightListener(onDrawableRightListener: OnDrawableRightListener) {
        this.onDrawableRightListener = onDrawableRightListener
    }

    interface OnDrawableClearListener {
        fun onDrawableClearClick()
    }
    private var onDrawableClearListener: OnDrawableClearListener? = null

    fun setOnDrawableClearListener(onDrawableClearListener: OnDrawableClearListener) {
        this.onDrawableClearListener = onDrawableClearListener
    }
}