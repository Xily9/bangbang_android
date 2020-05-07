package com.autowheel.bangbang.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import kotlinx.android.synthetic.main.layout_item_footer.view.*

/**
 * Created by Xily on 2019/2/28.
 */
class LoadMoreRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var footView: View? = null
    private var time: Long = 0
    private var loadMore = false
    private var isLoadingMore = false
    var loadingMoreListener: (() -> Unit)? = null
    fun enableLoadMore() {
        if (adapter == null) {
            throw Exception("请先定义adapter")
        } else if (adapter !is BaseAdapter<*>) {
            throw Exception("adapter必须继承于BaseAdapter")
        } else {
            loadMore = true
            footView =
                LayoutInflater.from(context).inflate(R.layout.layout_item_footer, this, false)
            val adapter = adapter as BaseAdapter<*>
            adapter.footerView = footView
            val layoutManager = layoutManager
            if (layoutManager is GridLayoutManager) {
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter.getItemViewType(position) == adapter.TYPE_FOOTER)
                            layoutManager.spanCount
                        else
                            layoutManager.spanSizeLookup.getSpanSize(position)
                    }
                }
            }
        }
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        val layoutManager = layoutManager
        if (layoutManager != null) {
            val count = layoutManager.itemCount
            val position = when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> throw Exception("只支持LinearLayoutManager与GridLayoutManager")
            }
            if (!isLoadingMore && System.currentTimeMillis() - time > 500 && dy > 0 && count < position + 3) {
                time = System.currentTimeMillis()
                loadingMoreListener?.invoke()
            }
        }
    }

    fun showLoading() {
        isLoadingMore = true
        footView?.apply {
            layout_default.visibility = View.GONE
            layout_loading.visibility = View.VISIBLE
            layout_error.visibility = View.GONE
        }
    }

    fun showError() {
        isLoadingMore = false
        footView?.apply {
            layout_error.visibility = View.VISIBLE
            layout_default.visibility = View.GONE
            layout_loading.visibility = View.GONE
        }
    }

    fun showComplete() {
        isLoadingMore = false
        footView?.apply {
            layout_error.visibility = View.GONE
            layout_loading.visibility = View.GONE
            layout_default.visibility = View.VISIBLE
        }
    }
}