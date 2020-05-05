package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachCommentBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_user_evaluate.*

/**
 * Created by Xily on 2020/5/5.
 */
class UserEvaluateAdapter(list: List<CoachCommentBean>?) : BaseAdapter<CoachCommentBean>(list) {
    var btnListener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_user_evaluate
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: CoachCommentBean) {
        holder.tv_title.text = "${value.publisher_nickname}|${value.name}"
        holder.tv_evaluation.text = value.text
        holder.tv_date.text = value.date
        Glide.with(context).load("$BASE_URL/user/avatar/${value.publisher_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher_round).into(holder.iv_avatar)
        holder.btn_add.setOnClickListener {
            btnListener.invoke(position)
        }
    }
}