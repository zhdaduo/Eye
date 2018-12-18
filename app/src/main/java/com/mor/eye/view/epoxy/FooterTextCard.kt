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
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID

@EpoxyModelClass(layout = R.layout.model_footer_text_card)
abstract class FooterTextCard(val context: Context) : EpoxyModelWithHolder<FooterHolder>() {
    @EpoxyAttribute
    var showArrow: Boolean? = null
    @EpoxyAttribute
    var footerText: CharSequence? = null
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onClick: android.view.View.OnClickListener? = null

    override fun bind(holder: FooterHolder) {
        if (showArrow!!) holder.headerView.drawableEnd = context.drawable(R.drawable.ic_action_arrow_right).apply { tint(DisplayUtils.getColor(context, R.color.control_color)) }

        val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        params.endToEnd = PARENT_ID
        params.topToTop = PARENT_ID
        params.bottomToBottom = PARENT_ID

        holder.headerView.apply {
            layoutParams = params
            setTextColor(DisplayUtils.getColor(context, R.color.control_color))
            textSize = 14F
        }
        holder.headerView.text = footerText
        holder.headerView.setOnClickListener(onClick)
    }
}

class FooterHolder : KotlinEpoxyHolder() {
    val headerView by bind<TextView>(R.id.header)
}