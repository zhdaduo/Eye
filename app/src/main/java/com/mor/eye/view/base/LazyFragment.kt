package com.mor.eye.view.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mor.eye.EyeApplication
import com.mor.eye.R
import com.mor.eye.repository.remote.RetrofitException
import com.mor.eye.util.ktx.doOnPreDraw
import com.mor.eye.util.other.DataSourceConstant
import com.mor.eye.util.other.showToast
import com.mor.eye.view.detail.activity.*
import com.mor.eye.view.gallery.GalleryActivity

abstract class LazyFragment : Fragment() {
    lateinit var callbacks: Callbacks
    private var isViewPrepared: Boolean = false
    private var hasFetchData: Boolean = false

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        return initView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCallback()
        init()
        isViewPrepared = true
        lazyFetchDataIfPrepared()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared()
        }
    }

    private fun lazyFetchDataIfPrepared() {
        if (userVisibleHint && !hasFetchData && isViewPrepared) {
            hasFetchData = true
            lazyFetchData()
        }
    }

    fun scheduleStartPostponedTransition(sharedElement: View) {
        sharedElement.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hasFetchData = false
        isViewPrepared = false
    }

    override fun onDestroy() {
        super.onDestroy()
        EyeApplication.refWatcher.watch(this)
    }

    private fun initCallback() {
        setCallback {
            categoryClick { id, iconUrl -> CategoriesDetailActivity.open(requireActivity(), id.toString(), iconUrl) }
            authorClick { id, userType -> AuthorDetailActivity.open(requireActivity(), id.toString(), userType) }
            videoClick { videoData -> VideoDetailActivity.open(requireActivity(), videoData) }
            dynamicInfoClick { requireActivity().showToast("dynamicInfo") }
            bannerClick { url ->
                if (url.contains(DataSourceConstant.EYEPETIZER)) {
                    WebDetailActivity.open(requireActivity(), url)
                } else {
                    val intent = Intent("android.intent.action.VIEW")
                    val uri = Uri.parse(url)
                    intent.data = uri
                    startActivity(intent)
                }
            }
            toggleClick { requireActivity().showToast("toggle") }
            tagClickListener { id -> TagsDetailActivity.open(requireActivity(), id.toString()) }
            pictureClick { index, urls, photo -> GalleryActivity.open(requireActivity(), index, urls, photo) }
            autoPlayVideoClick { _ -> requireActivity().showToast("autoPlayVideo") }
        }
    }

    fun setCallback(cb: Callbacks) {
        callbacks = cb
    }

    fun setCallback(callbacks: CallbacksExtensions.() -> Unit) {
        val cb = CallbacksExtensions()
        cb.callbacks()
        setCallback(cb)
    }

    abstract fun lazyFetchData()

    abstract fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    abstract fun init()

    protected fun showErrorToast(exception: Throwable) {
        if (exception is RetrofitException) {
            when (exception.getKind()) {

                RetrofitException.Kind.HTTP ->
                    requireActivity().showToast(R.string.default_http_error_message)

                RetrofitException.Kind.NETWORK ->
                    requireActivity().showToast(R.string.default_network_error_message)

                RetrofitException.Kind.UNEXPECTED ->
                    requireActivity().showToast(R.string.default_unexpected_error_message)
            }
        } else {
            requireActivity().showToast(R.string.default_http_error_message)
        }
    }
}