package com.mor.eye.view.gallery

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.OnViewTapListener
import com.github.chrisbanes.photoview.PhotoView
import com.mor.eye.R
import com.mor.eye.util.glide.loadImage

class FullSizePhotoAdapter(
        val onViewTapListener: OnViewTapListener,
        val dismissListener: DismissListener = {},
        val positionChangeListener: PositionChangeListener = { _, _, _ -> }
) : PagerAdapter() {

    private var urls: List<String>? = null

    fun submitList(urlList: List<String>?) {
        this.urls = urlList
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val root = LayoutInflater.from(container.context).inflate(R.layout.item_full_screen_photo, container, false)

        val flRoot = root.findViewById<FlingLayout>(R.id.fl_root).apply {
            this.dismissListener = this@FullSizePhotoAdapter.dismissListener
            this.positionChangeListener = this@FullSizePhotoAdapter.positionChangeListener
        }

        root.findViewById<PhotoView>(R.id.pv_photo).run {
            setOnScaleChangeListener { scaleFactor, _, _ -> flRoot.isDragEnabled = scaleFactor <= 1.5f }
            setOnViewTapListener(onViewTapListener)
            this.loadImage(this.context, urls?.get(position)!!)
        }

        container.addView(root)
        return root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = urls?.size ?: 0

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}