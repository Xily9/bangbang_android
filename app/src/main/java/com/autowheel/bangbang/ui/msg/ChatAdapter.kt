package com.autowheel.bangbang.ui.msg

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseAdapter
import com.autowheel.bangbang.model.DataManager
import com.autowheel.bangbang.model.network.bean.MessageBean
import com.autowheel.bangbang.utils.dp2px
import kotlinx.android.synthetic.main.item_chat.*
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(list: List<MessageBean>) : BaseAdapter<MessageBean>(list) {
    private var id = DataManager.profile.uid
    var rightDrawable: Drawable? = null
    var leftDrawable: Drawable? = null
    override fun getLayoutId(): Int {
        return R.layout.item_chat
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, value: MessageBean) {
        holder.apply {
            list?.let {
                if (position == 0 || value.timestamp - it[position - 1].timestamp > 300) {
                    tv_time.visibility = View.VISIBLE
                    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    val day2 =
                        SimpleDateFormat("dd", Locale.CHINA).format(value.timestamp * 1000L).toInt()
                    tv_time.text = if (day == day2) {
                        SimpleDateFormat("HH:mm", Locale.CHINA).format(value.timestamp * 1000L)
                    } else {
                        SimpleDateFormat(
                            "MM-dd HH:mm",
                            Locale.CHINA
                        ).format(value.timestamp * 1000L)
                    }
                } else {
                    tv_time.visibility = View.GONE
                }
            }
            if (value.from == id) {
                layout_right.visibility = View.VISIBLE
                layout_left.visibility = View.GONE
                right_msg.text = value.message
                rightDrawable?.let {
                    face_right.setImageDrawable(rightDrawable)
                }
            } else {
                layout_right.visibility = View.GONE
                layout_left.visibility = View.VISIBLE
                left_msg.text = value.message
                if (value.from == 0) {
                    face_left.setBackgroundResource(R.drawable.bg_round)
                    face_left.imageTintList = ColorStateList.valueOf(Color.WHITE)
                    face_left.setPadding(dp2px(10f), dp2px(10f), dp2px(10f), dp2px(10f))
                    face_left.setImageResource(R.drawable.ic_notifications_active_black_24dp)
                } else {
                    leftDrawable?.let {
                        face_left.setImageDrawable(leftDrawable)
                    }
                }
            }
        }
    }
}