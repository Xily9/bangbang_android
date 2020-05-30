package com.autowheel.bangbang.ui.admin.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.HelpApproveListBean
import kotlinx.android.synthetic.main.item_activity_admin_help_approve.*

/**
 * Created by Xily on 2020/5/2.
 */
class AdminHelpApproveAdapter(list: List<HelpApproveListBean>?) :
    BaseAdapter<HelpApproveListBean>(list) {
    var listener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_admin_help_approve
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: HelpApproveListBean
    ) {
        holder.tv_nickname.text = "被帮扶用户：${value.assisted_nickname}"
        holder.tv_name.text = "课程：${value.assisted_course}"
        holder.rv_approve_select.layoutManager = LinearLayoutManager(context)
        if (value.selectIndex == 0) {
            value.assistants[0].select = true
            value.selectIndex = 1
        }
        val adapter = AdminHelpApproveSelectAdapter(value.assistants)
        holder.rv_approve_select.adapter = adapter
        adapter.selectListener = {
            value.assistants[value.selectIndex - 1].select = false
            value.selectIndex = it + 1
            adapter.notifyDataSetChanged()
        }
        holder.btn_agree.setOnClickListener {
            listener.invoke(position)
        }
    }
}