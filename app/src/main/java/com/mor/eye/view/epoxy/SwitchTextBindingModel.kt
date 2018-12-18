package com.mor.eye.view.epoxy

import android.content.Context
import android.databinding.ViewDataBinding
import android.widget.TextView
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.mor.eye.BR
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
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
        binding.root.setting_open.apply {
            onSelect(this)
            onRelease(binding.root.setting_close)
            setOnClickListener(openClick)
        }
        binding.root.setting_close.apply {
            onSelect(this)
            onRelease(binding.root.setting_open)
            setOnClickListener(closeClick)
        }
    }

    private fun onSelect(textView: TextView) {
        textView.setTextColor(DisplayUtils.getColor(context, R.color.main_text_color))
    }

    private fun onRelease(textView: TextView) {
        textView.setTextColor(DisplayUtils.getColor(context, R.color.text_color))
    }

    override fun unbind(holder: DataBindingHolder) {
        openClick = null
        closeClick = null
    }
}