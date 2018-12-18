package com.mor.eye.view.home.mime

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannedString
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mor.eye.R
import com.mor.eye.actionText
import com.mor.eye.util.AppUtils
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.epoxy.withModels
import com.mor.eye.util.ktx.buildSpannedString
import com.mor.eye.util.ktx.color
import com.mor.eye.view.base.BaseFragment
import com.mor.eye.view.detail.SettingFragment
import com.mor.eye.view.home.MainFragment

class MimeFragment : BaseFragment() {
    private lateinit var rvAction: EpoxyRecyclerView
    private lateinit var tvVersion: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var tvLike: TextView
    private lateinit var tvPreload: TextView

    override fun getLayoutRes(): Int = R.layout.fragment_mime

    override fun initView(rootView: View) {
        rvAction = rootView.findViewById(R.id.action_rv)
        tvVersion = rootView.findViewById(R.id.tvVersion)
        ivAvatar = rootView.findViewById(R.id.ivAvatar)
        tvLike = rootView.findViewById(R.id.tvLike)
        tvPreload = rootView.findViewById(R.id.tvPreload)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = null
        iv_rightTitle?.setImageDrawable(DisplayUtils.getDrawable(requireActivity(), R.drawable.ic_action_more_fill_circle))
        iv_rightTitle?.visibility = View.VISIBLE
        initListener()
        tvVersion.text = getVersion()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        initRecyclerView()
    }

    override fun initLogic() {

    }

    override fun showToolBar(): Boolean {
        return true
    }

    private fun initRecyclerView() {
        rvAction.withModels {
            actionText {
                id(1)
                actionText(getString(R.string.mime_focus))
                actionClick { _ -> toast(R.string.mime_focus) }
            }

            actionText {
                id(2)
                actionText(getString(R.string.mime_view_history))
                actionClick { _ -> toast(R.string.mime_view_history) }
            }

            actionText {
                id(3)
                actionText(getString(R.string.mime_notification_switch))
                actionClick { _ -> toast(R.string.mime_notification_switch) }
            }

            actionText {
                id(4)
                actionText(getString(R.string.mime_report))
                actionClick { _ -> toast(R.string.mime_report) }
            }

            actionText {
                id(5)
                actionText(getString(R.string.mime_post))
                actionClick { _ -> toast(R.string.mime_post) }
            }
        }
    }

    private fun initListener() {
        iv_rightTitle?.setOnClickListener { (parentFragment as MainFragment).startBrotherFragment(SettingFragment.newInstance()) }
        ivAvatar.setOnClickListener { toast(R.string.login) }
        tvLike.setOnClickListener { toast(R.string.like) }
        tvPreload.setOnClickListener { toast(R.string.preload) }
    }

    private fun getVersion(): SpannedString {
        return buildSpannedString {
            color(DisplayUtils.getColor( requireActivity(), R.color.tv_hint)) {
                append(getString(R.string.version))
                append(" ")
                append(AppUtils.getVerName(requireActivity()).toString())
            }
        }
    }

    companion object {
        fun newInstance(): MimeFragment {
            return MimeFragment()
        }
    }
}