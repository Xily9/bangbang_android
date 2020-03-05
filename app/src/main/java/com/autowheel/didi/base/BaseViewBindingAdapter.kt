package com.autowheel.didi.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

/**
 * RecyclerView通用基类
 * @param <U> List容器的类型
 * @author Xily
 */

abstract class BaseViewBindingAdapter<T : ViewBinding, U>(var list: List<U>?) :
    RecyclerView.Adapter<BaseViewBindingAdapter.BaseViewHolder<T>>() {
    lateinit var context: Context
        private set
    private var onItemClickListener: ((position: Int) -> Unit?)? = null
    private var onItemLongClickListener: ((position: Int) -> Boolean?)? = null

    abstract fun initViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup): T

    fun setOnItemClickListener(onItemClickListener: (position: Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (position: Int) -> Boolean) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        context = parent.context
        val holder =
            BaseViewHolder(initViewBinding(LayoutInflater.from(context), parent))
        onItemClickListener?.let {
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(holder.adapterPosition)
            }
        }
        onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(holder.adapterPosition) ?: false
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        onBindViewHolder(holder.viewBinding, position, list!![position])
    }

    protected abstract fun onBindViewHolder(viewBinding: T, position: Int, value: U)

    class BaseViewHolder<T : ViewBinding>(val viewBinding: T) :
        RecyclerView.ViewHolder(viewBinding.root) {
    }
}
