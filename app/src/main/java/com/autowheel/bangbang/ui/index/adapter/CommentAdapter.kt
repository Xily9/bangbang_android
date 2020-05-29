package com.autowheel.bangbang.ui.index.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachCommentBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_activity_detail_comment.*

/**
 * Created by Xily on 2020/5/7.
 */
class CommentAdapter(list: List<CoachCommentBean>?) : BaseAdapter<CoachCommentBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_activity_detail_comment
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: CoachCommentBean) {
        Glide.with(context).load("$BASE_URL/user/avatar/${value.publisher_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(holder.iv_avatar)
        holder.tv_nickname.text = value.publisher_nickname
        holder.tv_content.text = value.text
        holder.tv_date.text = value.date
    }
}