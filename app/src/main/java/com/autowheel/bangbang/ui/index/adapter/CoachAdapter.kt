package com.autowheel.bangbang.ui.index.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_fragment_index.*

/**
 * Created by Xily on 2020/4/28.
 */
class CoachAdapter(list: List<CoachBean>?) : BaseAdapter<CoachBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_fragment_index
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: CoachBean) {
        Glide.with(context).load("$BASE_URL/user/avatar/${value.publisher_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher_round).into(holder.iv_avatar)
        holder.tv_nickname.text = value.publisher_nickname
        if (value.type == "course") {
            holder.tv_detail.text = "${value.name}(成绩${value.course_score})"
        } else {
            holder.tv_detail.text = value.name
        }
        holder.tv_declaration.text = value.declaration
    }
}