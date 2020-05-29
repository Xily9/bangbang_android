package com.autowheel.bangbang.ui.index.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.HelpBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_help.*

/**
 * Created by Xily on 2020/5/25.
 */
class HelpAdapter(list: List<HelpBean>?) : BaseAdapter<HelpBean>(list) {
    var applyListener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_help
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: HelpBean) {
        Glide.with(context).load("$BASE_URL/user/avatar/${value.user_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(holder.iv_avatar)
        holder.tv_nickname.text = value.user_nickname
        holder.tv_name.text = value.course
        holder.btn_apply.setOnClickListener {
            applyListener.invoke(position)
        }
    }
}