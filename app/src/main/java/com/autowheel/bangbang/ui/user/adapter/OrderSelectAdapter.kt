package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachBookListBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_order_select.*

/**
 * Created by Xily on 2020/5/2.
 */
class OrderSelectAdapter(list: List<CoachBookListBean.BookUserlist>?) :
    BaseAdapter<CoachBookListBean.BookUserlist>(list) {
    var selectListener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_order_select
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: CoachBookListBean.BookUserlist
    ) {
        holder.tv_nickname.text = value.nickname
        holder.rb_select.isChecked = value.select
        Glide.with(context).load("$BASE_URL/user/avatar/${value.uid}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher_round).into(holder.iv_avatar)
        holder.itemView.setOnClickListener {
            if (!value.select) {
                holder.rb_select.isChecked = true
                value.select = true
                selectListener.invoke(position)
            }
        }
    }
}