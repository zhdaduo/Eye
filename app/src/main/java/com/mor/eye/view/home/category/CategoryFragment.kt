package com.mor.eye.view.home.category

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.mor.eye.BriefCardBindingModel_
import com.mor.eye.R
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.databinding.convertStringToFace
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.BaseFragment
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.koin.android.viewmodel.ext.android.viewModel

class CategoryFragment: BaseFragment() {
    private val model: CategoryViewModel by viewModel()
    private var controller: CategoryController? = null
    private var items: MutableList<ItemListBean> = ArrayList()

    private var rvCategory: EpoxyRecyclerView? = null

    override fun getLayoutRes(): Int = R.layout.activity_category

    override fun initView(rootView: View) {
        rvCategory = rootView.findViewById(R.id.category_rv)
        controller = CategoryController(callbacks)
        setupToolbar()
        controller?.let { rvCategory?.setController(it) }
        initTouchHelper()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.apply {
            title = null
            setNavigationOnClickListener { requireActivity().onBackPressed() }
            setNavigationIcon(R.drawable.ic_arrow_left_black)
        }
        tv_title?.apply {
            setText(R.string.all_categories)
            visibility = View.VISIBLE
            typeface = convertStringToFace(getString(R.string.lan_ting_bold))
        }
    }

    override fun initLogic() {
        model.getCategoryData()
        model.uiLoadData.observeK(this) {
            items.clear()
            items.addAll(it!!)
            controller?.addLoadData(items)
        }
    }

    override fun showToolBar(): Boolean {
        return true
    }

    private fun initTouchHelper() {
        EpoxyTouchHelper.initDragging(controller)
                .withRecyclerView(rvCategory)
                .forVerticalList()
                .withTarget(BriefCardBindingModel_::class.java)
                .andCallbacks(object : EpoxyTouchHelper.DragCallbacks<BriefCardBindingModel_>() {

                    override fun onDragStarted(model: BriefCardBindingModel_, itemView: View, adapterPosition: Int) {
                        super.onDragStarted(model, itemView, adapterPosition)
                        itemView.animate().translationZBy(requireActivity().dpToPx(4).toFloat())
                    }

                    override fun onDragReleased(model: BriefCardBindingModel_, itemView: View) {
                        super.onDragReleased(model, itemView)
                        itemView.animate().translationZ(0f)
                    }

                    override fun onModelMoved(fromPosition: Int, toPosition: Int, modelBeingMoved: BriefCardBindingModel_?, itemView: View?) {
                        items.add(toPosition, items.removeAt(fromPosition))
                    }

                    override fun clearView(model: BriefCardBindingModel_?, itemView: View?) {
                        super.clearView(model, itemView)
                        controller?.addLoadData(items)
                    }
                })
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return FragmentAnimator(R.anim.screen_right_in, R.anim.screen_right_out ,R.anim.screen_null, R.anim.screen_null)
    }

    override fun onDestroyView() {
        rvCategory?.recycledViewPool?.clear()
        rvCategory?.adapter = null
        controller = null
        rvCategory = null
        toolbar?.setNavigationOnClickListener(null)
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): CategoryFragment {
            return CategoryFragment()
        }
    }
}