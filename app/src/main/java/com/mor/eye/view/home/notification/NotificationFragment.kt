package com.mor.eye.view.home.notification

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mor.eye.R
import com.mor.eye.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private lateinit var notificationBinding: FragmentNotificationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        return notificationBinding.root
    }

}