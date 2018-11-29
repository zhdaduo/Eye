package com.mor.eye.view.gallery

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.chrisbanes.photoview.OnViewTapListener
import com.mor.eye.R
import com.mor.eye.databinding.ActivityGalleryBinding
import com.mor.eye.repository.data.Photo
import com.mor.eye.ui.AndroidTagGroup
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.glide.loadWithCircle
import com.mor.eye.util.ktx.onPageChangeListener
import com.mor.eye.util.other.*
import com.mor.eye.util.other.GalleryArgumentConstant.Companion.EXTRA_LIST_URL
import com.mor.eye.util.other.GalleryArgumentConstant.Companion.EXTRA_PHOTO
import com.mor.eye.util.other.GalleryArgumentConstant.Companion.EXTRA_POSITION
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class GalleryActivity : AppCompatActivity() {

    private val toolbar: Toolbar by bindable(R.id.toolbar)
    private val rootView: View by bindable(R.id.fl_root)
    private val viewPager: ViewPager by bindable(R.id.vp_list)
    private val container: ConstraintLayout by bindable(R.id.text_contain)
    private val tvNum: TextView by bindable(R.id.tv_num)
    private val ivAvatar: ImageView by bindable(R.id.ivAvatar)
    private val btnFocus: Button by bindable(R.id.btn_focus)
    private val tvName: TextView by bindable(R.id.tv_name)
    private val tvDescription: TextView by bindable(R.id.tv_description)
    private val tagGroup: AndroidTagGroup by bindable(R.id.tag_group)
    private val tvLikeNum: TextView by bindable(R.id.tv_likeNum)
    private val tvReplyNum: TextView by bindable(R.id.tv_reply_num)
    private val tvPreload: TextView by bindable(R.id.tv_preload)
    private val tvShare: TextView by bindable(R.id.tv_share)

    private val systemUiSwitch by inject<DefaultSystemUiSwitch>()
    private val galleryViewModel: GalleryViewModel by viewModel()
    private var adapter: FullSizePhotoAdapter? = null

    private val currentPosition by unsafeLazy { intent.getIntExtra(EXTRA_POSITION, 0) }
    private val urls by unsafeLazy { intent.getStringArrayListExtra(EXTRA_LIST_URL) }
    private val photo by unsafeLazy { intent.getParcelableExtra<Photo>(EXTRA_PHOTO) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityGalleryBinding>(this, R.layout.activity_gallery)
        initToolbar(toolbar)
        initView()
        galleryViewModel.currentPosition = currentPosition

        initViewPager(viewPager, urls, galleryViewModel.currentPosition)
    }

    override fun onStart() {
        super.onStart()
        systemUiSwitch.setOnUiStateChangeListener(this::updateUiVisibility)
        updateUiVisibility(systemUiSwitch.isUiVisible)
    }

    private fun updateUiVisibility(visible: Boolean) {
        toolbar.apply {
            if (visible) this.revealSlide() else this.fadeSlideUp()
        }
        container.apply {
            if (visible) this.revealSlide() else this.fadeSlideDown()
        }
    }

    override fun onDestroy() {
        systemUiSwitch.setOnUiStateChangeListener(null)
        adapter?.submitList(null)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(DisplayUtils.getDrawable(this, R.mipmap.icon_detail_player_back))
    }

    private fun initView() {
        ivAvatar.loadWithCircle(this, photo.avatar)
        if (photo.isFollowed) {
            btnFocus.remove()
        } else {
            btnFocus.show()
        }
        tvName.text = photo.name
        tvDescription.text = photo.description
        tagGroup.setTags(photo.tags)
        tvLikeNum.text = photo.likeNum.toString()
        tvReplyNum.text = photo.replyNum.toString()
        tvPreload.setOnClickListener { showToast(getString(R.string.preload)) }
        tvShare.setOnClickListener { showToast(getString(R.string.share)) }
        viewPager.setOnClickListener {
            galleryViewModel.darkMode.value = !(galleryViewModel.darkMode.value ?: false)
        }
    }

    private fun initViewPager(viewPager: ViewPager, urls: List<String>, position: Int) = viewPager.run {
        adapter = FullSizePhotoAdapter(
                OnViewTapListener { _, _, _ ->
                    systemUiSwitch.run { isUiVisible = !isUiVisible }
                },
                { finishNoAnim() },
                { _, _, factor -> changeContentTransparency(factor) }
        ).also {
            this@GalleryActivity.adapter = it
            it.submitList(urls)
        }

        currentItem = position

        var firstIn = true
        viewPager.addOnPageChangeListener(onPageChangeListener()
                .scrolled { position, _, _ ->
                    galleryViewModel.currentPosition = position
                    if (firstIn) tvNum.text = String.format((position + 1).toString() + "/" + urls.size)
                }
                .selected { position ->
                    firstIn = false
                    tvNum.text = String.format((position + 1).toString() + "/" + urls.size)
                }
        )
    }

    private fun changeContentTransparency(factor: Float) {
        rootView.setBackgroundColor(Color.argb(Math.round(255 * (1f - factor)), 0, 0, 0))
        toolbar.alpha = 1f - factor * 3
        container.alpha = 1f - factor * 3
    }

    companion object {
        fun open(context: Context, position: Int, urls: ArrayList<String>, photo: Photo) = context.startKActivity(GalleryActivity::class) {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PHOTO, photo)
            putExtras(bundle)
            putExtra(EXTRA_POSITION, position)
            putStringArrayListExtra(EXTRA_LIST_URL, urls)
        }
    }
}
