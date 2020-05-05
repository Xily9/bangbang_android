package com.autowheel.bangbang.ui.msg.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.MessageBean
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.dp2px
import com.autowheel.bangbang.utils.toTimeStr
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_fragment_message.*

/**
 * Created by Xily on 2020/5/3.
 */
class MsgAdapter(list: List<MessageBean>?) : BaseAdapter<MessageBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_fragment_message
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: MessageBean) {
        if (value.id == 0 || value.from == 0) {
            holder.iv_avatar.setBackgroundResource(R.drawable.bg_round)
            holder.iv_avatar.imageTintList = ColorStateList.valueOf(Color.WHITE)
            holder.iv_avatar.setPadding(dp2px(10f), dp2px(10f), dp2px(10f), dp2px(10f))
            holder.iv_avatar.setImageResource(R.drawable.ic_notifications_active_black_24dp)
        } else {
            holder.iv_avatar.background = null
            holder.iv_avatar.imageTintList = null
            holder.iv_avatar.setPadding(0, 0, 0, 0)
            Glide.with(context).load("$BASE_URL/user/avatar/${value.from}")
                .signature(ObjectKey(UserUtil.avatarUpdateTime))
                .error(R.mipmap.ic_launcher_round).into(holder.iv_avatar)
        }
        holder.tv_nickname.text = value.fromNickname
        holder.tv_message.text = value.message
        holder.tv_time.text = value.timestamp.toTimeStr()
    }
}