package com.mor.eye.ui.bottombar

import android.content.Context
import android.support.annotation.ColorInt
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import me.majiajie.pagerbottomtabstrip.internal.RoundMessageView
import android.widget.TextView
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem


class SpecialTab @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseTabItem(context, attrs, defStyleAttr) {

    private val mIcon: ImageView
    private val mTitle: TextView
    private val mMessages: RoundMessageView

    private var mDefaultDrawable: Drawable? = null
    private var mCheckedDrawable: Drawable? = null

    private var mChecked: Boolean = false

    private var mDefaultTextColor = DisplayUtils.getColor(context, R.color.tv_hint)
    private var mCheckedTextColor = DisplayUtils.getColor(context, R.color.colorBlack)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_special_tab, this, true)

        mIcon = findViewById(R.id.icon)
        mTitle = findViewById(R.id.title)
        mMessages = findViewById(R.id.messages)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        val view = getChildAt(0)
        view?.setOnClickListener(l)
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    fun initialize(@DrawableRes drawableRes: Int, @DrawableRes checkedDrawableRes: Int, title: String) {
        mDefaultDrawable = ContextCompat.getDrawable(context, drawableRes)
        mCheckedDrawable = ContextCompat.getDrawable(context, checkedDrawableRes)
        mTitle.text = title
    }

    override fun setChecked(checked: Boolean) {
        if (checked) {
            mIcon.setImageDrawable(mCheckedDrawable)
            mTitle.setTextColor(mCheckedTextColor)
        } else {
            mIcon.setImageDrawable(mDefaultDrawable)
            mTitle.setTextColor(mDefaultTextColor)
        }
        mChecked = checked
    }

    override fun setMessageNumber(number: Int) {
        mMessages.messageNumber = number
    }

    override fun setHasMessage(hasMessage: Boolean) {
        mMessages.setHasMessage(hasMessage)
    }

    override fun setTitle(title: String) {
        mTitle.text = title
    }

    override fun setDefaultDrawable(drawable: Drawable) {
        mDefaultDrawable = drawable;
        if (!mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    override fun setSelectedDrawable(drawable: Drawable) {
        mCheckedDrawable = drawable;
        if (mChecked) {
            mIcon.setImageDrawable(drawable);
        }
    }

    override fun getTitle(): String {
        return mTitle.text.toString()
    }

    fun setTextDefaultColor(@ColorInt color: Int) {
        mDefaultTextColor = color
    }

    fun setTextCheckedColor(@ColorInt color: Int) {
        mCheckedTextColor = color
    }
}