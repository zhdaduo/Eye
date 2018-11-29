package com.mor.eye.view.home.mime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mor.eye.R
import com.mor.eye.actionText
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.epoxy.withModels
import com.mor.eye.util.ktx.buildSpannedString
import com.mor.eye.util.ktx.color
import com.mor.eye.util.other.showToast
import com.mor.eye.view.base.BaseFragment
import com.mor.eye.view.detail.activity.SettingActivity
import kotlinx.android.synthetic.main.fragment_mime.*

class MimeFragment : BaseFragment() {
    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_mime, null)

    override fun init() {
        category_rv.withModels {
            actionText {
                id(1)
                actionText(getString(R.string.mime_focus))
                actionClick { _ -> requireActivity().showToast(getString(R.string.mime_focus)) }
            }

            actionText {
                id(2)
                actionText(getString(R.string.mime_view_history))
                actionClick { _ -> requireActivity().showToast(getString(R.string.mime_view_history)) }
            }

            actionText {
                id(3)
                actionText(getString(R.string.mime_notification_switch))
                actionClick { _ -> requireActivity().showToast(getString(R.string.mime_notification_switch)) }
            }

            actionText {
                id(4)
                actionText(getString(R.string.mime_report))
                actionClick { _ -> requireActivity().showToast(getString(R.string.mime_report)) }
            }

            actionText {
                id(5)
                actionText(getString(R.string.mime_post))
                actionClick { _ -> requireActivity().showToast(getString(R.string.mime_post)) }
            }
        }

        tvVersion.text = buildSpannedString {
            color(DisplayUtils.getColor(requireContext(), R.color.tv_hint)) {
                append(getString(R.string.version))
                append(" ")
                append(packageCode().toString())
            }
        }

        ivAvatar.setOnClickListener { requireActivity().showToast(getString(R.string.login)) }
        tvLike.setOnClickListener { requireActivity().showToast(getString(R.string.like)) }
        tvPreload.setOnClickListener { requireActivity().showToast(getString(R.string.preload)) }
        ivActionMore.setOnClickListener { SettingActivity.open(requireContext()) }
    }

    private fun packageCode(): Int {
        val manager = requireContext().packageManager
        val info = manager.getPackageInfo(requireContext().packageName, 0)
        return info.versionCode
    }
}