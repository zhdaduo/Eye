package com.mor.eye.view.detail.activity

import android.content.Context
import android.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.mor.eye.BR
import com.mor.eye.R
import kotlinx.android.synthetic.main.model_switch_text.view.*

@EpoxyModelClass(layout = R.layout.model_switch_text)
abstract class SwitchTextBindingModel(val context: Context) : DataBindingEpoxyModel() {
    @EpoxyAttribute
    var settingTitle: String? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var openClick: android.view.View.OnClickListener? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var closeClick: android.view.View.OnClickListener? = null

    override fun setDataBindingVariables(binding: ViewDataBinding) {
        binding.setVariable(BR.settingTitle, settingTitle)
        binding.setVariable(BR.openClick, openClick)
        binding.setVariable(BR.closeClick, closeClick)
        binding.root.setting_open.setOnClickListener {
            binding.root.setting_open.isSelected = true

            binding.root.setting_close.clearFocus()
        }
        binding.root.setting_close.setOnClickListener {
            binding.root.setting_close.requestFocus()
            binding.root.setting_open.clearFocus()
        }
    }
}