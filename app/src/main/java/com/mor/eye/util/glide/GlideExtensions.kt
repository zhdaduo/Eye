package com.mor.eye.util.glide

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

fun ImageView.loadPlaceWithColor(ctx: Context, url: String, color: Int) {
    GlideApp.with(ctx).asDrawable()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .transition(withCrossFade())
            .thumbnail(
                    GlideApp.with(ctx).asDrawable()
                            .load(ColorDrawable(DisplayUtils.getColor(ctx, color)))
                            .dontAnimate()
            )
            .into(this)
}

fun ImageView.loadPlaceWithIcon(ctx: Context, url: String, defaultIconId: Int) {
    GlideApp.with(ctx).asDrawable()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(defaultIconId)
            .error(defaultIconId)
            .transition(withCrossFade())
            .into(this)
}

fun ImageView.loadImageBlur(ctx: Context, url: String) {
    val requestOption = RequestOptions().
            diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .placeholder(ColorDrawable(DisplayUtils.getColor(ctx, R.color.detail_bg2)))

    GlideApp.with(ctx)
            .load(url)
            .apply(requestOption)
            .into(this)
}

fun ImageView.loadImage(ctx: Context, url: String) {
    GlideApp.with(ctx).asDrawable()
            .load(url)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(ColorDrawable(DisplayUtils.getColor(ctx, R.color.detail_bg2)))
            .into(this)
}

fun ImageView.loadImageCircle(ctx: Context, url: String) {
    GlideApp.with(ctx).asDrawable()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .transition(withCrossFade())
            .apply(RequestOptions.circleCropTransform())
            .thumbnail(
                    GlideApp.with(ctx).asDrawable()
                            .load(ColorDrawable(DisplayUtils.getColor(ctx, R.color.detail_bg2)))
                            .dontAnimate()
                            .apply(RequestOptions.circleCropTransform())
            )
            .into(this)
}

fun ImageView.loadImageRound(ctx: Context, url: String, radius: Int) {
    GlideApp.with(ctx).asDrawable()
            .load(url)
            .transition(withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .apply(bitmapTransform(MultiTransformation(CenterCrop(),
                    RoundedCornersTransformation(radius, 0,
                            RoundedCornersTransformation.CornerType.ALL))))
            .thumbnail(
                    GlideApp.with(ctx).asDrawable()
                            .load(ColorDrawable(DisplayUtils.getColor(ctx, R.color.detail_bg2)))
                            .dontAnimate()
                            .apply(bitmapTransform(MultiTransformation(CenterCrop(),
                                    RoundedCornersTransformation(radius, 0,
                                            RoundedCornersTransformation.CornerType.ALL))))
            )
            .into(this)
}

fun ImageView.loadPlaceWithColor(view: ImageView, url: String, color: Int) {
    GlideApp.with(view).asDrawable()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(ColorDrawable(DisplayUtils.getColor(view.context, R.color.detail_bg2)))
            .dontAnimate()
            .apply(RequestOptions.bitmapTransform(ColorFilterTransformation(color)))
            .into(this)
}

fun ImageView.loadPlaceWithIcon(view: ImageView, url: String, defaultIconId: Int) {
    GlideApp.with(view).asDrawable()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(defaultIconId)
            .error(defaultIconId)
            .transition(withCrossFade())
            .into(this)
}

fun ImageView.loadImageCircle(view: ImageView, url: String) {
    GlideApp.with(view).asDrawable()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .transition(withCrossFade())
            .apply(RequestOptions.circleCropTransform())
            .thumbnail(
                    GlideApp.with(view).asDrawable()
                            .load(ColorDrawable(DisplayUtils.getColor(view.context, R.color.detail_bg2)))
                            .dontAnimate()
                            .apply(RequestOptions.circleCropTransform())
            )
            .into(this)
}

fun ImageView.loadImageRound(view: ImageView, url: String, radius: Int) {
    GlideApp.with(view).asDrawable()
            .load(url)
            .transition(withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .apply(bitmapTransform(MultiTransformation(CenterCrop(),
                    RoundedCornersTransformation(radius, 0,
                            RoundedCornersTransformation.CornerType.ALL))))
            .thumbnail(
                    GlideApp.with(view).asDrawable()
                            .load(ColorDrawable(DisplayUtils.getColor(view.context, R.color.detail_bg2)))
                            .dontAnimate()
                            .apply(bitmapTransform(MultiTransformation(CenterCrop(),
                                    RoundedCornersTransformation(radius, 0,
                                            RoundedCornersTransformation.CornerType.ALL))))
            )
            .into(this)
}

fun ImageView.loadLocalImage(ctx: Context, @DrawableRes id: Int) {
    GlideApp.with(ctx).asDrawable()
            .load(id)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(ColorDrawable(DisplayUtils.getColor(ctx, R.color.detail_bg2)))
            .transition(withCrossFade())
            .into(this)
}
