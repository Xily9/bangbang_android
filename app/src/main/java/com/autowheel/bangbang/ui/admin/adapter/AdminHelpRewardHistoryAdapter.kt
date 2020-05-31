package com.autowheel.bangbang.ui.admin.adapter

import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.HelpRewardHistoryBean
import kotlinx.android.synthetic.main.item_activity_admin_help_reward_history.*

/**
 * Created by Xily on 2020/4/25.
 */
class AdminHelpRewardHistoryAdapter(list: List<HelpRewardHistoryBean>?) :
    BaseAdapter<HelpRewardHistoryBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_activity_admin_help_reward_history
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: HelpRewardHistoryBean
    ) {
        holder.tv_sno.text = value.sno
        holder.tv_name.text = value.name
    }
}