package com.autowheel.bangbang.ui.admin.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.HelpApproveListBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_admin_help_approve_select.*

/**
 * Created by Xily on 2020/5/2.
 */
class AdminHelpApproveSelectAdapter(list: List<HelpApproveListBean.Assistant>?) :
    BaseAdapter<HelpApproveListBean.Assistant>(list) {
    var selectListener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_admin_help_approve_select
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: HelpApproveListBean.Assistant
    ) {
        holder.tv_name.text = value.assistant_nickname
        holder.rb_choose.isChecked = value.select
        holder.tv_grade.text = "成绩：${value.grade}"
        holder.tv_subject.text = "关联课程名：${value.course}"
        holder.tv_ps.text = "附言：${value.complement}"
        Glide.with(context).load("$BASE_URL/user/avatar/${value.assistant_user_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(holder.iv_avatar)
        holder.itemView.setOnClickListener {
            if (!value.select) {
                holder.rb_choose.isChecked = true
                value.select = true
                selectListener.invoke(position)
            }
        }
    }
}