package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachBean
import kotlinx.android.synthetic.main.item_activity_user_coach.*

/**
 * Created by Xily on 2020/5/2.
 */
class UserCoachAdapter(list: List<CoachBean>?) : BaseAdapter<CoachBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_activity_user_coach
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: CoachBean) {
        holder.tv_name.text =
            if (value.type == "course") "${value.name}(成绩{${value.course_score}})" else value.name
        holder.tv_declaration.text = value.declaration
        holder.tv_time.text = value.release_time
    }
}