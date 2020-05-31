package com.autowheel.bangbang.ui.admin.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.HelpRewardListBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_admin_help_reward.*

/**
 * Created by Xily on 2020/5/31.
 */
class AdminHelpRewardAdapter(list: List<HelpRewardListBean>?) :
    BaseAdapter<HelpRewardListBean>(list) {
    var btnPickUpListener: ((Int) -> Unit) = {}
    var btnAgreeListener: ((Int) -> Unit) = {}
    var btnDisagreeListener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_admin_help_reward
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: HelpRewardListBean
    ) {
        Glide.with(context).load("$BASE_URL/user/avatar/${value.assistant_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(holder.iv_avatar)
        Glide.with(context).load("$BASE_URL/user/avatar/${value.assisted_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(holder.iv_avatar2)
        holder.tv_nickname.text = value.assistant_nickname
        holder.tv_nickname2.text = value.assisted_nickname
        holder.tv_course.text = value.course
        holder.tv_day.text = "${value.days}天"
        holder.btn_agree.setOnClickListener {
            btnAgreeListener.invoke(position)
        }
        holder.btn_disagree.setOnClickListener {
            btnDisagreeListener.invoke(position)
        }
        holder.btn_pickup.setOnClickListener {
            btnPickUpListener.invoke(position)
        }
    }
}