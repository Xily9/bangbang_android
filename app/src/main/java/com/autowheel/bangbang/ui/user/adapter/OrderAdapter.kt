package com.autowheel.bangbang.ui.user.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.network.bean.CoachBookListBean
import kotlinx.android.synthetic.main.item_activity_order.*

/**
 * Created by Xily on 2020/5/2.
 */
class OrderAdapter(list: List<CoachBookListBean>?) : BaseAdapter<CoachBookListBean>(list) {
    var listener: ((Int) -> Unit) = {}
    override fun getLayoutId(): Int {
        return R.layout.item_activity_order
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: CoachBookListBean) {
        holder.tv_name.text = value.name
        holder.rv_order_select.layoutManager = LinearLayoutManager(context)
        if (value.selectIndex == 0) {
            value.book_userlist[0].select = true
            value.selectIndex = 1
        }
        val adapter = OrderSelectAdapter(value.book_userlist)
        holder.rv_order_select.adapter = adapter
        adapter.selectListener = {
            value.book_userlist[value.selectIndex - 1].select = false
            value.selectIndex = it + 1
            adapter.notifyDataSetChanged()
        }
        holder.btn_agree.setOnClickListener {
            listener.invoke(position)
        }
    }
}