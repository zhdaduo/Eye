package com.mor.eye.view.home.community

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alibaba.android.vlayout.VirtualLayoutAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.mor.eye.R
import com.mor.eye.ui.ImageNice9Layout
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.glide.loadImage

class ImageMultiVAdapter(layoutManager: VirtualLayoutManager, val context: Context, private val itemMargin: Int): VirtualLayoutAdapter<ImageMultiVAdapter.ImageViewHolder>(layoutManager) {
    private var pictures = mutableListOf<String>()
    private var mItemDelegate: ImageNice9Layout.ItemDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageMultiVAdapter.ImageViewHolder = ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mulit_image, null))

    override fun getItemCount(): Int = pictures.size

    override fun onBindViewHolder(holder: ImageMultiVAdapter.ImageViewHolder, position: Int) {
        val layoutParams = VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val width: Int
        val height: Int
        val imageCount = pictures.size
        val displayW = DisplayUtils.getScreenWidth(context)
        val imageUrl = pictures[position]
        if (imageCount == 1) {
            width = displayW
            height = width
        } else if (imageCount == 2) {
            width = displayW / 2
            height = width
        } else if (imageCount == 3) {
            if (position == 0) {
                width = (displayW * 0.66).toInt()
                height = width
                layoutParams.rightMargin = itemMargin
                layoutParams.bottomMargin = itemMargin
            } else {
                if (position == 1 || position == 2) {
                    if (position == 1) {
                        layoutParams.bottomMargin = itemMargin / 2
                    } else {
                        layoutParams.bottomMargin = itemMargin
                    }
                }
                width = ((displayW * 0.33).toInt())
                height = width
            }
        } else if (imageCount == 4) {
            if (position == 0) {
                width = displayW
                height = ((width * 0.5).toInt())
            } else {
                width = ((displayW * 0.33).toInt())
                height = width
            }
        } else if (imageCount == 5) {
            if (position == 0 || position == 1) {
                width = ((displayW * 0.5).toInt())
                height = width
            } else {
                width = ((displayW * 0.33).toInt())
                height = width
            }
        } else if (imageCount == 6) {
            if (position == 0) {
                width = ((displayW * 0.66).toInt())
                height = width
                layoutParams.rightMargin = itemMargin
                layoutParams.bottomMargin = itemMargin
            } else {
                if (position == 1 || position == 2) {
                    if (position == 1) {
                        layoutParams.bottomMargin = itemMargin / 2
                    } else {
                        layoutParams.bottomMargin = itemMargin
                    }

                }
                width = ((displayW * 0.33).toInt())
                height = width
            }
        } else if (imageCount == 7) {
            if (position == 0) {
                width = displayW
                height = ((width * 0.5).toInt())
            } else {
                width = ((displayW * 0.33).toInt())
                height = width
            }
        } else if (imageCount == 8) {
            if (position == 0 || position == 1) {
                width = ((displayW * 0.5).toInt())
                height = width
            } else {
                width = ((displayW * 0.33).toInt())
                height = width
            }
        } else {
            width = ((displayW * 0.33).toInt())
            height = width
        }
        layoutParams.width = width
        layoutParams.height = height
        holder.itemView.layoutParams = layoutParams
        holder.itemView.setOnClickListener { mItemDelegate?.onItemClick(position) }
        holder.mImageView.loadImage(context, imageUrl)
    }

    fun bindData(pictures: MutableList<String>) {
        this.pictures = pictures
        notifyDataSetChanged()
    }

    fun setItemDelegate(itemDelegate: ImageNice9Layout.ItemDelegate) {
        this.mItemDelegate = itemDelegate
    }

    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val mImageView = itemView.findViewById<ImageView>(R.id.item_multi_image)
    }
}