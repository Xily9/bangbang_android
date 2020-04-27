package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.GradeBean
import kotlinx.android.synthetic.main.item_activity_user_study.*

/**
 * Created by Xily on 2020/4/25.
 */
class UserStudyAdapter(list: List<GradeBean>?) : BaseAdapter<GradeBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_activity_user_study
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: GradeBean) {
        holder.tv_grade.text = value.point
        holder.tv_name.text = value.name
    }
}