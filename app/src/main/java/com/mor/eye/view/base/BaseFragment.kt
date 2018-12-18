package com.mor.eye.view.base

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.mor.eye.EyeApplication
import me.yokeyword.fragmentation.SupportFragment
import com.gyf.barlibrary.ImmersionBar
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.FrameLayout
import android.view.ViewStub
import com.mor.eye.R
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import com.mor.eye.repository.remote.RetrofitException
import com.mor.eye.util.other.DataSourceConstant
import com.mor.eye.util.other.showToast
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.detail.activity.*
import com.mor.eye.view.gallery.GalleryActivity

abstract class BaseFragment : SupportFragment() {
    protected var mImmersionBar: ImmersionBar? = null
    protected var rootView: View? = null
    protected var toolbar: Toolbar? = null
    protected var tv_title: TextView? = null
    protected var tv_rightTitle: TextView? = null
    protected var iv_rightTitle: ImageView? = null
    private var ll_base_root: LinearLayout? = null
    protected val callbacks: Callbacks by unsafeLazy {
        CallbacksExtensions()
                .categoryClick { id, iconUrl -> CategoriesDetailActivity.open(requireActivity(), id.toString(), iconUrl) }
                .authorClick { id, userType -> AuthorDetailActivity.open(requireActivity(), id.toString(), userType) }
                .videoClick { videoId, feedUrl, playUrl, blurUrl, videoTitle -> VideoDetailActivity.open(requireActivity(), videoId, feedUrl, playUrl, blurUrl, videoTitle) }
                .dynamicInfoClick { requireActivity().showToast("dynamicInfo") }
                .bannerClick { url ->
                    if (url.contains(DataSourceConstant.EYEPETIZER)) {
                        WebDetailActivity.open(requireActivity(), url)
                    } else {
                        val intent = Intent("android.intent.action.VIEW")
                        val uri = Uri.parse(url)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
                .toggleClick { requireActivity().showToast("toggle") }
                .tagClickListener { id -> TagsDetailActivity.open(requireActivity(), id.toString()) }
                .pictureClick { index, urls, photo -> GalleryActivity.open(requireActivity(), index, urls, photo) }
                .autoPlayVideoClick { _ -> requireActivity().showToast("autoPlayVideo") }
    }

    fun getLl_base_root(): LinearLayout {
        return ll_base_root!!
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (rootView == null) {
            //为空时初始化。
            if (showToolBar() && getToolBarResId() != 0) {
                //如果需要显示自定义toolbar,并且资源id存在的情况下，实例化baseView;
                rootView = inflater.inflate(if (toolbarCover())
                    R.layout.ac_base_toolbar_cover
                else
                    R.layout.ac_base, container, false)//根布局
                ll_base_root = rootView?.findViewById(R.id.ll_base_root) as LinearLayout
                val mVs_toolbar = rootView?.findViewById(R.id.vs_toolbar) as ViewStub//toolbar容器
                val fl_container = rootView?.findViewById(R.id.fl_container) as FrameLayout//子布局容器
                mVs_toolbar.layoutResource = getToolBarResId()//toolbar资源id
                mVs_toolbar.inflate()//填充toolbar
                inflater.inflate(getLayoutRes(), fl_container, true)//子布局
            } else {
                //不显示通用toolbar
                rootView = inflater.inflate(getLayoutRes(), container, false)

            }
        }
        initToolBar(rootView!!)
        initView(rootView!!)
        return rootView!!
    }

    /**
     * 初始化toolbar可重写覆盖自定的toolbar,base中实现的是通用的toolbar
     */
    open fun initToolBar(rootView: View) {
        toolbar = rootView.findViewById(R.id.toolbar)
        if (toolbar == null) {
            return
        }
        tv_title = toolbar?.findViewById(R.id.toolbar_title)
        tv_rightTitle = toolbar?.findViewById(R.id.tv_toolbar_right)
        iv_rightTitle = toolbar?.findViewById(R.id.iv_toolbar_right)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initLogic()
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (immersionEnabled()) {
            mImmersionBar = ImmersionBar.with(this)
            immersionInit(mImmersionBar!!)
        }
        hideInput()
    }

    /**
     * 逻辑内容初始化，懒加载模式
     */
    protected abstract fun initLogic()

    override fun onSupportInvisible() {
        super.onSupportInvisible()
    }

    /**
     * 关闭软键盘
     */
    fun hideInput() {
        if (requireActivity().currentFocus == null) return
        (requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow( requireActivity().currentFocus.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onDestroyView() {
        rootView = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mImmersionBar?.destroy()
        EyeApplication.refWatcher.watch(this)
    }

    /**
     * 当前页面Fragment支持沉浸式初始化。默认返回false，可设置支持沉浸式初始化
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    open fun immersionEnabled(): Boolean {
        return false
    }

    /**
     * 状态栏初始化（immersionEnabled默认为false时不走该方法）
     */
    open fun immersionInit(mImmersionBar: ImmersionBar) {}

    /**
     * 是否显示通用toolBar
     */
    open fun showToolBar(): Boolean {
        return false
    }

    /**
     * 获取自定义toolbarview 资源id 默认为0，showToolBar()方法必须返回true才有效
     */
    open fun getToolBarResId(): Int {
        return R.layout.layout_common_toolbar
    }

    /**
     * toolbar是否覆盖在内容区上方
     *
     * @return false 不覆盖  true 覆盖
     */
    open fun toolbarCover(): Boolean {
        return false
    }

    /**
     * 获取布局文件
     */
    protected abstract fun getLayoutRes(): Int

    /**
     * 初始化view
     */
    protected abstract fun initView(rootView: View)

    protected fun toast(msgId: Int) {
        requireActivity().showToast(msgId)
    }

    protected fun showErrorToast(exception: Throwable) {
        if (exception is RetrofitException) {
            when (exception.getKind()) {

                RetrofitException.Kind.HTTP ->
                    toast(R.string.default_http_error_message)

                RetrofitException.Kind.NETWORK ->
                    toast(R.string.default_network_error_message)

                RetrofitException.Kind.UNEXPECTED ->
                    toast(R.string.default_unexpected_error_message)
            }
        } else {
            toast(R.string.default_http_error_message)
        }
    }
}