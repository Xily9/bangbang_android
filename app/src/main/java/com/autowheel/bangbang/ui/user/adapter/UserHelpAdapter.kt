package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.UserHelpBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_user_help.*

/**
 * Created by Xily on 2020/5/25.
 */
class UserHelpAdapter(list: List<UserHelpBean>?) : BaseAdapter<UserHelpBean>(list) {
    var btnPickListener = fun(_: Int) {}
    var btnApplyListener = fun(_: Int) {}
    var btnChatListener = fun(_: Int) {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_user_help
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: UserHelpBean) {
        Glide.with(context).load("$BASE_URL/user/avatar/${value.assisted_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(holder.iv_avatar)
        holder.tv_name.text = value.assisted_nickname
        holder.tv_subject.text = value.course
        holder.btn_pickup.setOnClickListener {
            btnPickListener.invoke(position)
        }
        holder.btn_apply.setOnClickListener {
            btnApplyListener.invoke(position)
        }
        holder.btn_chat.setOnClickListener {
            btnChatListener.invoke(position)
        }
    }
}