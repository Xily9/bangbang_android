package com.autowheel.bangbang.ui.note.adapter

import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.bean.NoteTagBean
import com.autowheel.bangbang.utils.gone
import com.autowheel.bangbang.utils.visible
import kotlinx.android.synthetic.main.item_fragment_note_type.*

/**
 * Created by Xily on 2020/5/6.
 */
class TagAdapter(list: List<NoteTagBean>?) : BaseAdapter<NoteTagBean>(list) {
    override fun getLayoutId(): Int {
        return R.layout.item_fragment_note_type
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: NoteTagBean) {
        if (value.isSelected) {
            holder.layout_select.visible()
            holder.layout_unselect.gone()
            holder.tv_name.text = value.name
        } else {
            holder.layout_select.gone()
            holder.layout_unselect.visible()
            holder.tv_name_2.text = value.name
        }
    }
}