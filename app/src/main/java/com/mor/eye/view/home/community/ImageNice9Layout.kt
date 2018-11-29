package com.mor.eye.view.home.community

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.other.unsafeLazy
import java.util.*


/**
 * Created by wxy on 2017/5/25.
 * 模仿nice首页列表 9种样式图片
 * 依赖淘宝vLayout开源控件 实现
 * 1
 * -------------------------
 * |                       |
 * |                       |
 * |           1           |
 * |                       |
 * |                       |
 * |                       |
 * -------------------------
 *
 *
 * 2
 * * -------------------------
 * |           |           |
 * |           |           |
 * |           |           |
 * |     1     |     2     |
 * |           |           |
 * |           |           |
 * |           |           |
 * -------------------------
 * 3
 * -------------------------
 * |           |           |
 * |           |     2     |
 * |           |           |
 * |     1     |-----------|
 * |           |           |
 * |           |     3     |
 * |           |           |
 * -------------------------
 * 4
 * -------------------------
 * |                       |
 * |           1           |
 * |                       |
 * |-----------------------|
 * |      |        |       |
 * |   2  |     3  |    4  |
 * |      |        |       |
 * -------------------------
 * 5
 * -------------------------
 * |          |            |
 * |    1     |   2        |
 * |          |            |
 * |-----------------------|
 * |      |        |       |
 * |   3  |    4   |    5  |
 * |      |        |       |
 * -------------------------
 * 6
 * -------------------------
 * |           |           |
 * |           |     2     |
 * |           |           |
 * |     1     |-----------|
 * |           |           |
 * |           |     3     |
 * |           |           |
 * -------------------------
 * |      |        |       |
 * |   4  |   5    |    6  |
 * |      |        |       |
 * -------------------------
 *
 *
 * 7
 * -------------------------
 * |                       |
 * |           1           |
 * |                       |
 * |-----------------------|
 * |      |        |       |
 * |   2  |     3  |    4  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   5  |     6  |    7  |
 * |      |        |       |
 * -------------------------
 * 8
 * -------------------------
 * |          |            |
 * |    1     |   2        |
 * |          |            |
 * |-----------------------|
 * |      |        |       |
 * |   3  |    4   |    5  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   6  |     7  |    8  |
 * |      |        |       |
 * -------------------------
 * 9
 * |-----------------------|
 * |      |        |       |
 * |   1  |     2  |    3  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   4  |     5  |    6  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   7  |     8  |    9  |
 * |      |        |       |
 * -------------------------
 */

class ImageNice9Layout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var mRecycler: RecyclerView? = null
    private val layoutManager: VirtualLayoutManager by unsafeLazy { VirtualLayoutManager(context) }
    private val gridLayoutHelper: GridLayoutHelper by unsafeLazy { GridLayoutHelper(spanCount) }
    private val onePlusHelper: OnePlusNLayoutHelper by unsafeLazy { OnePlusNLayoutHelper(itemCount) }
    private var helpers: MutableList<LayoutHelper>? = null
    private val mMultiVAdapter: ImageMultiVAdapter by unsafeLazy { ImageMultiVAdapter(layoutManager, context, itemMargin) }

    init {
        initView(context)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageNice9Layout)
        val num = typedArray.indexCount//取得本集合里面总共有多少个属性
        for (i in 0 until num) {//遍历这些属性，拿到对应的属性
            initAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
    }

    private fun initAttr(attr: Int, typedArray: TypedArray) {
        if (attr == R.styleable.ImageNice9Layout_nice9_itemMargin) {
            itemMargin = typedArray.getDimension(attr, 5f).toInt()
        }
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_imagemulit_layout, this)
        mRecycler = view.findViewById(R.id.drag_recycler)
        mRecycler?.setHasFixedSize(true)
        mRecycler?.layoutManager = layoutManager
        mRecycler?.isNestedScrollingEnabled = false
    }

    fun setItemDelegate(itemDelegate: ItemDelegate) {
        mMultiVAdapter.setItemDelegate(itemDelegate)
    }

    //绑定数据，根据数据，先行计算recyclerview高度，固定高度，防止多重滑动时候冲突
    fun bindData(pictures: List<String>?) {
        if (pictures != null) {
            helpers = LinkedList()
            gridLayoutHelper.setGap(itemMargin)
            gridLayoutHelper.hGap = itemMargin
            val num = pictures.size
            val layoutParams = mRecycler!!.layoutParams
            val displayW = DisplayUtils.getDisplayWidth(context) - 2 * DisplayUtils.dpToPx(context, 16)
            layoutParams.width = displayW
            val height: Int
            if (num == 1) {
                height = layoutParams.width
            } else if (num == 2) {
                height = (displayW * 0.5).toInt()
            } else if (num == 3) {
                height = (displayW * 0.66).toInt() - itemMargin - itemMargin / 2
            } else if (num == 4) {
                height = (displayW * 0.5).toInt() + itemMargin + (displayW * 0.33).toInt()
            } else if (num == 5) {
                height = (displayW * 0.5).toInt() + itemMargin + (displayW * 0.33).toInt()
            } else if (num == 6) {
                height = (displayW * 0.66).toInt() + (displayW * 0.33).toInt() - itemMargin / 2
            } else if (num == 7 || num == 8) {
                height = (displayW * 0.5).toInt() + 2 * itemMargin + (displayW * 0.33).toInt() * 2
            } else {
                height = (displayW * 0.33 * 3 + 3 * itemMargin - itemMargin / 2).toInt()
            }
            layoutParams.height = height
            mRecycler!!.layoutParams = layoutParams
            //根据数量和位置 设置span占比
            gridLayoutHelper.setSpanSizeLookup(object : GridLayoutHelper.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (num == 1) {
                        6
                    } else if (num == 2) {
                        3
                    } else if (num == 3) {
                        2
                    } else if (num == 4) {
                        if (position == 0) {
                            6
                        } else {
                            2
                        }
                    } else if (num == 5) {
                        if (position == 0 || position == 1) {
                            3
                        } else {
                            2
                        }
                    } else if (num == 6) {
                        2
                    } else if (num == 7) {
                        if (position == 0) {
                            6
                        } else {
                            2
                        }
                    } else if (num == 8) {
                        if (position == 0 || position == 1) {
                            3
                        } else {
                            2
                        }
                    } else {
                        2
                    }
                }
            })
            helpers!!.clear()
            if (num == 3) {
                helpers!!.add(onePlusHelper)
                gridLayoutHelper.itemCount = 0
            } else {
                if (num == 6) {
                    helpers!!.add(onePlusHelper)
                    gridLayoutHelper.itemCount = 3
                } else {
                    gridLayoutHelper.itemCount = num
                }
                helpers!!.add(gridLayoutHelper)
            }
            layoutManager.setLayoutHelpers(helpers)
            mMultiVAdapter.bindData(pictures as MutableList<String>)
            mRecycler!!.adapter = mMultiVAdapter
        }
    }

    interface ItemDelegate {
        fun onItemClick(position: Int)
    }

    companion object {
        var itemMargin = 10
        const val spanCount = 6
        const val itemCount = 3
    }
}