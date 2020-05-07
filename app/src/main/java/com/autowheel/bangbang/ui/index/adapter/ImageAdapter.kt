package com.autowheel.bangbang.ui.index.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.autowheel.bangbang.R
import com.youth.banner.adapter.BannerAdapter


/**
 * Created by Xily on 2020/5/7.
 */
class ImageAdapter(datas: MutableList<String>) :
    BannerAdapter<String, ImageAdapter.ViewHolder>(datas) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ViewHolder(imageView)
    }

    override fun onBindView(holder: ViewHolder, data: String, position: Int, size: Int) {
        holder.imageView.setImageResource(
            when (position) {
                0 -> R.drawable.iv_login
                else -> R.drawable.iv_login_2
            }
        )
    }

    class ViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}