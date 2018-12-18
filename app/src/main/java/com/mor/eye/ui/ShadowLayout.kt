package com.mor.eye.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mor.eye.R

class ShadowLayout : FrameLayout {

    var mShadowColor: Int = 0
    var mShadowRadius: Float = 0.toFloat()
    var mCornerRadius: Float = 0.toFloat()
    var mDx: Float = 0.toFloat()
    var mDy: Float = 0.toFloat()
    var mBackgroundColor: Int = 0

    private var mInvalidateShadowOnSizeChanged = true
    private var mForceInvalidateShadow = false

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && (background == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false
            setBackgroundCompat(w, h)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false
            setBackgroundCompat(right - left, bottom - top)
        }
    }

    fun setInvalidateShadowOnSizeChanged(invalidateShadowOnSizeChanged: Boolean) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged
    }

    fun invalidateShadow() {
        mForceInvalidateShadow = true
        requestLayout()
        invalidate()
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(context, attrs)
        refreshPadding()
    }

    fun refreshPadding() {
        val xPadding = (mShadowRadius + Math.abs(mDx)).toInt()
        val yPadding = (mShadowRadius + Math.abs(mDy)).toInt()
        setPadding(xPadding, yPadding, xPadding, yPadding)
    }

    private fun setBackgroundCompat(w: Int, h: Int) {
        val bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowRadius, mDx, mDy, mShadowColor, Color.TRANSPARENT)
        val drawable = BitmapDrawable(resources, bitmap)
        background = drawable
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = getTypedArray(context, attrs, R.styleable.ShadowLayout) ?: return
        try {
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_radius, 0f)
            mShadowRadius = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_blur, 0f)
            mDx = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_offsetX, 0f)
            mDy = attr.getDimension(R.styleable.ShadowLayout_shadow_layout_offsetY, 0f)
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_shadow_layout_color, Color.parseColor("#22000000"))
            mBackgroundColor = attr.getColor(R.styleable.ShadowLayout_shadow_layout_background_color, Integer.MIN_VALUE)
        } finally {
            attr.recycle()
        }
    }

    private fun getTypedArray(context: Context, attributeSet: AttributeSet?, attr: IntArray): TypedArray? {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
    }

    private fun createShadowBitmap(
            shadowWidth: Int,
            shadowHeight: Int,
            cornerRadius: Float,
            shadowRadius: Float,
            dx: Float,
            dy: Float,
            shadowColor: Int,
            fillColor: Int
    ): Bitmap {
        val output: Bitmap = if (mBackgroundColor != Integer.MIN_VALUE) {
            Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ALPHA_8)
        }
        val canvas = Canvas(output)

        val shadowRect = RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius)

        if (dy > 0) {
            shadowRect.top += dy
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy)
            shadowRect.bottom -= Math.abs(dy)
        }

        if (dx > 0) {
            shadowRect.left += dx
            shadowRect.right -= dx
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx)
            shadowRect.right -= Math.abs(dx)
        }

        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = fillColor
        paint.style = Paint.Style.FILL

        paint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, paint)
        if (mBackgroundColor != Integer.MIN_VALUE) {
            paint.clearShadowLayer()
            paint.color = Color.WHITE
            val backgroundRect = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
            canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, paint)
        }

        return output
    }
}