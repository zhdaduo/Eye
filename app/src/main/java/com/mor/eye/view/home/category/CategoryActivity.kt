package com.mor.eye.view.home.category

import android.content.Context
import android.view.View
import com.airbnb.epoxy.EpoxyTouchHelper
import com.mor.eye.BriefCardBindingModel_
import com.mor.eye.R
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.dpToPx
import com.mor.eye.util.other.show
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_category.*
import org.koin.android.viewmodel.ext.android.viewModel

class CategoryActivity : BaseActivity() {
    private val model: CategoryViewModel by viewModel()
    private val controller by unsafeLazy { CategoryController(callbacks) }
    private var items: MutableList<ItemListBean> = ArrayList()

    override fun getLayout(): Int = R.layout.activity_category

    override fun init() {
        overridePendingTransition(R.anim.screen_right_in, R.anim.screen_null)
        initToolbar()
        tvBoldTitle.apply {
            show()
            setText(R.string.all_categories)
        }
        model.getCategoryData()
        observeViewModel()
        initRecyclerView()
        initTouchHelper()
    }

    private fun initRecyclerView() {
        category_rv.setController(controller)
    }

    private fun observeViewModel() {
        model.uiLoadData.observeK(this) {
            items.clear()
            items.addAll(it!!)
            controller.setData(items)
        }
    }

    private fun initTouchHelper() {
        EpoxyTouchHelper.initDragging(controller)
                .withRecyclerView(category_rv)
                .forVerticalList()
                .withTarget(BriefCardBindingModel_::class.java)
                .andCallbacks(object : EpoxyTouchHelper.DragCallbacks<BriefCardBindingModel_>() {

                    override fun onDragStarted(model: BriefCardBindingModel_, itemView: View, adapterPosition: Int) {
                        super.onDragStarted(model, itemView, adapterPosition)
                        itemView.animate().translationZBy(this@CategoryActivity.dpToPx(4).toFloat())
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
                        controller.setData(items)
                    }
                })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.screen_null, R.anim.screen_right_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        category_rv.adapter = null
    }

    companion object {
        fun open(context: Context) = context.startKActivity(CategoryActivity::class) {}
    }
}
