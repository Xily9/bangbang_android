package com.autowheel.bangbang.ui.msg.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.bean.MessageEventBean
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.MessageBean
import com.autowheel.bangbang.ui.msg.adapter.ChatAdapter
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.gone
import com.autowheel.bangbang.utils.toastError
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.jeremyliao.liveeventbus.LiveEventBus
import com.vanniktech.emoji.EmojiPopup
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/4/5.
 */
class ChatActivity : BackBaseActivity() {
    private var id = -1
    private var list = arrayListOf<MessageBean>()
    private lateinit var adapter: ChatAdapter
    private lateinit var emojiPopup: EmojiPopup
    override fun getToolbarTitle(): String {
        return if (id == 0) "系统消息" else ""
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_chat
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", -1)
        initLiveEventBus()
        initRecycleView()
        getNickName()
        loadData()
        if (id == 0) {
            layout_chat.gone()
        } else {
            getAvatar()
            layout_send.setOnClickListener {
                if (input_text.text.toString() == "") {
                    input_text.error = "消息不能为空!"
                } else {
                    val messageBean = MessageBean(
                        UserUtil.profile.uid,
                        "",
                        id,
                        (System.currentTimeMillis() / 1000).toInt(),
                        input_text.text.toString()
                    )
                    LiveEventBus.get("messageEvent", MessageEventBean::class.java)
                        .post(MessageEventBean().apply {
                            type = 1
                            message = input_text.text.toString()
                        })
                    list.add(messageBean)
                    input_text.setText("")
                    adapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }
            emojiPopup = EmojiPopup.Builder.fromRootView(ll_root).build(input_text)
            layout_emoji.setOnClickListener {
                if (emojiPopup.isShowing) {
                    emojiPopup.dismiss()
                    iv_emoji.setImageResource(R.drawable.ic_tag_faces_white_24dp)
                } else {
                    emojiPopup.toggle()
                    iv_emoji.setImageResource(R.drawable.ic_keyboard_white_24dp)
                }
            }
        }
    }

    private fun initLiveEventBus() {
        LiveEventBus.get("message", MessageBean::class.java).observe(this, Observer {
            list.add(it)
            adapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun getNickName() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getProfile(id)
            if (result.code == 0) {
                val data = result.data
                toolbar_title.text = data.nickname
            }
        })
    }

    private fun getAvatar() {
        Glide.with(this).load("$BASE_URL/user/avatar/${id}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    adapter.leftDrawable = resource
                    adapter.notifyDataSetChanged()
                }
            })
        Glide.with(this).load("$BASE_URL/user/avatar/${UserUtil.profile.uid}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    adapter.rightDrawable = resource
                    adapter.notifyDataSetChanged()
                }
            })
    }

    private fun initRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        adapter = ChatAdapter(list)
        recyclerView.adapter = adapter
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getHistoryMsg(id)
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            toastError("网络请求出错!")
        })
    }

    override fun onResume() {
        super.onResume()
        LiveEventBus.get("messageEvent", MessageEventBean::class.java)
            .post(MessageEventBean().apply {
                type = 2
                isChatActivity = true
                chatId = id
            })
    }

    override fun onPause() {
        super.onPause()
        LiveEventBus.get("messageEvent", MessageEventBean::class.java)
            .post(MessageEventBean().apply {
                type = 2
                isChatActivity = false
            })
    }

    override fun onDestroy() {
        if (id > 0 && emojiPopup.isShowing) {
            emojiPopup.dismiss()
        }
        super.onDestroy()
    }
}