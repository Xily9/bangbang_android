package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachBean
import kotlinx.android.synthetic.main.item_activity_user_coach_record.*

/**
 * Created by Xily on 2020/5/2.
 */
class UserCoachRecordAdapter(list: List<CoachBean>?) : BaseAdapter<CoachBean>(list) {

    override fun getLayoutId(): Int {
        return R.layout.item_activity_user_coach_record
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: CoachBean) {
        holder.tv_name.text = value.publisher_nickname
        holder.tv_detail.text =
            if (value.type == "course") "${value.name}(成绩{${value.course_score}})" else value.name
        holder.tv_time.text = value.release_time
        if (value.is_pay) {
            holder.btn_action1.text = "再次预约"
            holder.btn_action2.text = "评价"
        } else {
            holder.btn_action1.text = "取消预约"
            holder.btn_action2.text = "立即付款"
        }
    }
}