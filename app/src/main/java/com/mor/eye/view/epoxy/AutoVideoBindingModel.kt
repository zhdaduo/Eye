package com.mor.eye.view.epoxy

import android.content.Context
import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.mor.eye.R
import kotlinx.android.synthetic.main.model_auto_video.view.*
import com.mor.eye.BR
import com.mor.eye.ui.AndroidTagGroup
import com.mor.eye.ui.CornerOutlineProvider
import com.mor.eye.util.NetworkUtils
import com.shuyu.gsyvideoplayer.GSYVideoManager

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

    override fun setDataBindingVariables(binding: ViewDataBinding) {
        binding.root.video_item_player.apply {
            loadThumbImage(pictureUrl)
            setUp(videoUrl, true, "")
            showTime(timeText)
            outlineProvider = CornerOutlineProvider()
            clipToOutline = true
            if (NetworkUtils.is4G(context)) {
                show4gPlay()
            }
        }

        tagGroupList?.apply { binding.root.video_tag_group?.setTags(this) }
        binding.root.video_tag_group.setOnTagClickListener(tagListener)
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

    override fun unbind(holder: DataBindingHolder) {
        GSYVideoManager.releaseAllVideos()
        holder.dataBinding.root.video_tag_group.removeAllViews()
    }
}