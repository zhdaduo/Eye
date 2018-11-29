package com.mor.eye.view.home.newcommunity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mor.eye.R
import com.mor.eye.databinding.FragmentNewCommunityBinding

class NewCommunityFragment : Fragment() {
    private lateinit var communityBinding: FragmentNewCommunityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        communityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_community, container, false)
        return communityBinding.root
    }

}