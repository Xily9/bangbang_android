package com.autowheel.bangbang.ui.admin.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.HelpRewardPickUpBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import kotlinx.android.synthetic.main.item_activity_admin_help_reward_pickup.*

/**
 * Created by Xily on 2020/5/31.
 */
class AdminHelpRewardPickUpAdapter(list: List<HelpRewardPickUpBean>?) :
    BaseAdapter<HelpRewardPickUpBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_activity_admin_help_reward_pickup
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        value: HelpRewardPickUpBean
    ) {
        Glide.with(context).load(
            GlideUrl(
                "$BASE_URL/admin/file/${value.file_id}",
                LazyHeaders.Builder().addHeader(
                    "Cookie",
                    UserUtil.token.split(";")[0] + ";"
                ).build()
            )
        )
            .into(holder.iv_image)
        holder.tv_time.text = value.date
    }

}