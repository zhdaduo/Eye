package com.mor.eye.ui.bottombar

import android.support.v4.content.ContextCompat
import android.support.annotation.DrawableRes
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import com.mor.eye.R

import me.majiajie.pagerbottomtabstrip.internal.RoundMessageView
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem

/**
 * Created by mjj on 2017/6/3
 */
class SpecialTabRound @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseTabItem(context, attrs, defStyleAttr) {

    private val mIcon: ImageView
    private val mMessages: RoundMessageView

    private var mDefaultDrawable: Drawable? = null

    init {

        LayoutInflater.from(context).inflate(R.layout.item_special_tab_round, this, true)

        mIcon = findViewById(R.id.icon)
        mMessages = findViewById(R.id.messages)
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    fun initialize(@DrawableRes drawableRes: Int) {
        mDefaultDrawable = ContextCompat.getDrawable(context, drawableRes)
    }

    override fun setChecked(checked: Boolean) {
        // no need
    }

    override fun setMessageNumber(number: Int) {
        mMessages.messageNumber = number
    }

    override fun setHasMessage(hasMessage: Boolean) {
        mMessages.setHasMessage(hasMessage)
    }

    override fun setTitle(title: String) {
        // no need
    }

    override fun setDefaultDrawable(drawable: Drawable) {
        mIcon.setImageDrawable(drawable)
    }

    override fun setSelectedDrawable(drawable: Drawable) {
        // no need
    }

    override fun getTitle(): String {
        return "no title"
    }
}