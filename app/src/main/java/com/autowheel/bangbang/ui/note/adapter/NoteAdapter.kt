package com.autowheel.bangbang.ui.note.adapter

import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.NoteBean
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_fragment_note_content.*

/**
 * Created by Xily on 2020/5/5.
 */
class NoteAdapter(list: List<NoteBean>?) : BaseAdapter<NoteBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_fragment_note_content
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: NoteBean) {
        Glide.with(context).load("$BASE_URL/user/avatar/${value.publisher_id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher_round).into(holder.iv_avatar)
        holder.tv_author.text = "作者：${value.publisher_nickname}"
        holder.tv_title.text = value.title
        holder.tv_content.text = value.content
        holder.tv_good_number.text = value.compliments.toString()
    }
}