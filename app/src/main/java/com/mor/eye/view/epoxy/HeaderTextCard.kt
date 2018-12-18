package com.mor.eye.view.epoxy

import android.content.Context
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.databinding.drawable
import com.mor.eye.util.databinding.drawableEnd
import com.mor.eye.util.databinding.tint
import com.mor.eye.view.base.KotlinEpoxyHolder

@EpoxyModelClass(layout = R.layout.model_header_text_card)
abstract class HeaderTextCard(val context: Context) : EpoxyModelWithHolder<HeaderHolder>() {
    @EpoxyAttribute
    var headerDefaultColor: Boolean? = null
    @EpoxyAttribute
    var showArrow: Boolean? = null
    @EpoxyAttribute
    var headerText: CharSequence? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClick: android.view.View.OnClickListener? = null

    override fun bind(holder: HeaderHolder) {
        if (showArrow!!) holder.headerView.drawableEnd = context.drawable(R.drawable.ic_action_arrow_right).apply { tint(DisplayUtils.getColor(context, R.color.detail_bg2)) }
        if (!headerDefaultColor!!) holder.headerView.setTextColor(DisplayUtils.getColor(context, R.color.colorWhite))
        holder.headerView.text = headerText
        holder.headerView.setOnClickListener(onClick)
    }
}

class HeaderHolder : KotlinEpoxyHolder() {
    val headerView by bind<TextView>(R.id.header)
}