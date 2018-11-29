package com.mor.eye.view.home.community

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import android.widget.ImageView
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.mor.eye.BR
import com.mor.eye.R
import com.mor.eye.ui.AndroidTagGroup
import com.mor.eye.util.glide.loadImage
import com.mor.eye.util.ktx.afterMeasured
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import kotlinx.android.synthetic.main.model_auto_video.view.*

@EpoxyModelClass(layout = R.layout.model_auto_video)
abstract class AutoVideoBindingModel(val context: Context) : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var pictureUrl: String? = null
    @EpoxyAttribute
    var videoUrl: String? = null
    @EpoxyAttribute
    var coverUrl: String? = null
    @EpoxyAttribute
    var timeText: String? = null
    @EpoxyAttribute
    var titleText: String? = null
    @EpoxyAttribute
    var subTitleText: CharSequence? = null
    @EpoxyAttribute
    var descriptionText: String? = null
    @EpoxyAttribute
    var likeNumText: CharSequence? = null
    @EpoxyAttribute
    var commitNumText: CharSequence? = null
    @EpoxyAttribute
    var dateText: String? = null

    @EpoxyAttribute
    var tagGroupList: MutableList<String>? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var tagListener: AndroidTagGroup.OnTagClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var authorClick: android.view.View.OnClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var autoPlayClick: android.view.View.OnClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var toggleClick: android.view.View.OnClickListener? = null

    private val gsyVideoOptionBuilder: GSYVideoOptionBuilder = GSYVideoOptionBuilder()
    private val imageView: ImageView = ImageView(context)

    override fun setDataBindingVariables(binding: ViewDataBinding) {
        binding.root.afterMeasured {
            initVideoPlayer(binding)
            binding.root.video_item_player.outlineProvider = CornerOutlineProvider()
            binding.root.video_item_player.clipToOutline = true
        }

        tagGroupList?.apply { binding.root.video_tag_group?.setTags(this) }
        binding.root.video_tag_group.setOnTagClickListener(tagListener)
        binding.setVariable(BR.timeText, timeText)
        binding.setVariable(BR.descriptionText, descriptionText)
        binding.setVariable(BR.coverUrl, coverUrl)
        binding.setVariable(BR.titleText, titleText)
        binding.setVariable(BR.subTitleText, subTitleText)
        binding.setVariable(BR.likeNumText, likeNumText)
        binding.setVariable(BR.commitNumText, commitNumText)
        binding.setVariable(BR.dateText, dateText)
        binding.setVariable(BR.authorClick, authorClick)
        binding.setVariable(BR.autoPlayClick, autoPlayClick)
        binding.setVariable(BR.toggleClick, toggleClick)
    }

    private fun initVideoPlayer(binding: ViewDataBinding) {
        // 增加封面
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        pictureUrl?.let { imageView.loadImage(context, it) }
        val gsyVideoPlayer = binding.root.video_item_player
        if (imageView.parent != null) {
            val viewGroup = imageView.parent as ViewGroup
            viewGroup.removeView(imageView)
        }
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(imageView)
                .setUrl(videoUrl)
                .setSetUpLazy(true)
                .setCacheWithPlay(true)
                .setLockLand(true)
                .setPlayTag(TAG)
                .setLooping(true)
                .setStartAfterPrepared(true)
                .build(gsyVideoPlayer)
    }

    override fun unbind(holder: DataBindingHolder) {
        GSYVideoManager.releaseAllVideos()
        holder.dataBinding.root.video_tag_group.removeAllViews()
    }

    companion object {
        const val TAG = "RecyclerViewList"
    }
}