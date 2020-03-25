package com.autowheel.bangbang.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

/**
 * RecyclerView通用基类
 * @param <U> List容器的类型
 * @author Xily
 */

abstract class BaseAdapter<U>(var list: List<U>?) :
        RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {
    lateinit var context: Context
        private set
    private var onItemClickListener: ((position: Int) -> Unit?)? = null
    private var onItemLongClickListener: ((position: Int) -> Boolean?)? = null
    var headerView: View? = null
        set(value) {
            field = value
            notifyItemInserted(0)
        }
    var footerView: View? = null
        set(value) {
            field = value
            notifyItemInserted(itemCount - 1)
        }

    val TYPE_HEADER = 0
    val TYPE_FOOTER = 1
    val TYPE_NORMAL = 2

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun setOnItemClickListener(onItemClickListener: (position: Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (position: Int) -> Boolean) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun getItemCount(): Int {
        var size = list?.size ?: 0
        if (headerView == null && footerView == null)
            return size
        if (headerView != null)
            size++
        if (footerView != null)
            size++
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (headerView == null && footerView == null) {
            return TYPE_NORMAL
        }
        if (headerView != null && position == 0) {
            return TYPE_HEADER
        }
        if (footerView != null && position == itemCount - 1) {
            return TYPE_FOOTER
        }
        return TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        headerView?.let {
            if (viewType == TYPE_HEADER)
                return BaseViewHolder(it)
        }
        footerView?.let {
            if (viewType == TYPE_FOOTER)
                return BaseViewHolder(it)
        }
        val holder =
                BaseViewHolder(LayoutInflater.from(context).inflate(getLayoutId(), parent, false))
        onItemClickListener?.let {
            holder.itemView.setOnClickListener {
                val position = if (headerView != null)
                    holder.adapterPosition - 1
                else
                    holder.adapterPosition
                onItemClickListener?.invoke(position)
            }
        }
        onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                val position = if (headerView != null)
                    holder.adapterPosition - 1
                else
                    holder.adapterPosition
                onItemLongClickListener?.invoke(position) ?: false
            }
        }
        return holder
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (headerView != null)
                onBindViewHolder(holder, position - 1, list!![position - 1])
            else
                onBindViewHolder(holder, position, list!![position])
        }
    }

    protected abstract fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: U)

    @ContainerOptions(CacheImplementation.SPARSE_ARRAY)
    class BaseViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer
}
