package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_user_coach_record.*

/**
 * Created by Xily on 2020/5/2.
 */
class UserCoachRecordAdapter(list: List<CoachBean>?) : BaseAdapter<CoachBean>(list) {
    var btn1Listener: ((Int) -> Unit) = {}
    var btn2Listener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_user_coach_record
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: CoachBean) {
        holder.tv_name.text = value.publisher_nickname
        holder.tv_detail.text = value.name
        holder.tv_time.text = value.release_time
        holder.tv_price.text = "￥${value.price}"
        holder.tv_all_price.text = "￥${value.price}"
        Glide.with(context).load("$BASE_URL/user/avatar/${value.publisher_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher_round).into(holder.iv_avatar)
        holder.tv_name.text = value.publisher_nickname
        holder.btn_action1.setOnClickListener {
            btn1Listener.invoke(position)
        }
        holder.btn_action2.setOnClickListener {
            btn2Listener.invoke(position)
        }
        if (value.is_pay) {
            holder.btn_action1.text = "再次预约"
            holder.btn_action2.text = "评价"
            holder.tv_status.text = "交易成功"
            //holder.layout_delete.visible()
        } else {
            holder.btn_action1.text = "取消预约"
            holder.btn_action2.text = "立即付款"
            holder.tv_status.text = "待付款"
            //holder.layout_delete.gone()
        }
    }
}