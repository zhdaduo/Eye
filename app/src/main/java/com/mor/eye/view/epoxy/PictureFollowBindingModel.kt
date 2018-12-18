package com.mor.eye.view.epoxy

import android.content.Context
import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.mor.eye.R
import com.mor.eye.BR
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.repository.data.Photo
import com.mor.eye.ui.AndroidTagGroup
import com.mor.eye.ui.CornerOutlineProvider
import com.mor.eye.ui.ImageNice9Layout
import com.mor.eye.util.ktx.afterMeasured
import com.mor.eye.view.base.Callbacks
import kotlinx.android.synthetic.main.model_pictue_follow.view.*

@EpoxyModelClass(layout = R.layout.model_pictue_follow)
abstract class PictureFollowBindingModel(val context: Context) : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var item: ItemListBean? = null
    @EpoxyAttribute
    var coverUrl: String? = null
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
    var imgGroupList: MutableList<Nothing>? = null
    @EpoxyAttribute
    var tagGroupList: MutableList<String>? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var tagListener: AndroidTagGroup.OnTagClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var callback: Callbacks? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var toggleClick: android.view.View.OnClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var authorClick: android.view.View.OnClickListener? = null

    override fun setDataBindingVariables(binding: ViewDataBinding) {
        binding.root.afterMeasured {
            binding.root.nine_image.apply {
                outlineProvider = CornerOutlineProvider()
                clipToOutline = true

                bindData(imgGroupList)
                setItemDelegate(object: ImageNice9Layout.ItemDelegate {
                    override fun onItemClick(position: Int) {
                        val ownerBean = item?.data?.content?.data?.owner!!
                        val consumptionBean = item?.data?.content?.data?.consumption!!
                        callback?.onPictureClickListener(position, item?.data?.content?.data?.urls,
                                Photo(ownerBean.avatar, ownerBean.nickname, item?.data?.content?.data?.description!!,
                                        ownerBean.followed, consumptionBean.collectionCount, consumptionBean.replyCount, tagGroupList?.toList()!!))
                    }
                })
            }
        }

        tagGroupList?.apply { binding.root.picture_tag_group?.setTags(this) }
        binding.root.picture_tag_group.setOnTagClickListener(tagListener)
        binding.setVariable(BR.coverUrl, coverUrl)
        binding.setVariable(BR.descriptionText, descriptionText)
        binding.setVariable(BR.titleText, titleText)
        binding.setVariable(BR.subTitleText, subTitleText)
        binding.setVariable(BR.likeNumText, likeNumText)
        binding.setVariable(BR.commitNumText, commitNumText)
        binding.setVariable(BR.dateText, dateText)
        binding.setVariable(BR.toggleClick, toggleClick)
        binding.setVariable(BR.authorClick, authorClick)
    }

    override fun unbind(holder: DataBindingHolder) {
        holder.dataBinding.root.picture_tag_group.removeAllViews()
    }
}