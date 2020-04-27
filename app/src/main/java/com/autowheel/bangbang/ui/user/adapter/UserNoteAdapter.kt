package com.autowheel.bangbang.ui.user.adapter

import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.UserNoteBean
import kotlinx.android.synthetic.main.item_activity_user_note.*

/**
 * Created by Xily on 2020/4/26.
 */
class UserNoteAdapter(list: List<UserNoteBean>?) : BaseAdapter<UserNoteBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_activity_user_note
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: UserNoteBean) {
        holder.tv_title.text = value.title
        holder.tv_content.text = value.content
    }
}