package com.autowheel.bangbang.ui.msg.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.DataManager
import com.autowheel.bangbang.model.bean.MessageEventBean
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.MessageBean
import com.autowheel.bangbang.ui.msg.adapter.ChatAdapter
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.gone
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.google.gson.Gson
import com.jeremyliao.liveeventbus.LiveEventBus
import com.vanniktech.emoji.EmojiPopup
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/4/5.
 */
class ChatActivity : BackBaseActivity() {
    var isLoadingMore = false
    var totalDataCount = 0
    var page = 1
    private var id = -1
    private var itemId = 0
    private var list = arrayListOf<MessageBean>()
    private lateinit var adapter: ChatAdapter
    private lateinit var emojiPopup: EmojiPopup
    override fun getToolbarTitle(): String {
        return ""
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_chat
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", -1)
        itemId = intent.getIntExtra("itemId", 0)
        initLiveEventBus()
        initRecycleView()
        if (id == 0) {
            layout_chat.gone()
        }
        if (id >= 0) {
            getNickName()
            getAvatar()
            getItem()
            loadData()
            layout_send.setOnClickListener {
                if (input_text.text.toString() == "") {
                    input_text.error = "消息不能为空!"
                } else {
                    val messageBean = MessageBean(
                        DataManager.profile.uid,
                        "",
                        id,
                        (System.currentTimeMillis() / 1000).toInt(),
                        input_text.text.toString(),
                        itemId
                    )
                    LiveEventBus.get("messageEvent", MessageEventBean::class.java)
                        .post(MessageEventBean().apply {
                            type = 1
                            message = Gson().toJson(messageBean)
                        })
                    list.add(messageBean)
                    input_text.setText("")
                    adapter.notifyDataSetChanged()
                    recyclerView.scrollToPosition(adapter.itemCount - 1)
                }
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

    private fun initLiveEventBus() {
        LiveEventBus.get("message", MessageBean::class.java).observe(this, Observer {
            list.add(it)
            adapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })
    }

    private fun getNickName() {
        if (id > 0)
            launch(tryBlock = {
                val result = RetrofitHelper.getApiService().getProfile(id)
                if (result.code == 0) {
                    val data = result.data
                    toolbar_title.text = data.nickname
                }
            })
        else
            toolbar_title.text = "系统消息"
    }

    private fun getAvatar() {
        if (id > 0) {
            Glide.with(this).load("$BASE_URL/user/avatar/${id}")
                .signature(ObjectKey(UserUtil.avatarUpdateTime))
                .error(R.mipmap.ic_launcher_round).into(object : CustomTarget<Drawable>() {
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
        }
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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = layoutManager.findFirstVisibleItemPosition()
                val count = layoutManager.itemCount
                if (!isLoadingMore && totalDataCount > count && dy > 0 && position < 5) {
                    page++
                    loadData()
                }
            }
        })
    }

    private fun loadData() {
        /*RetrofitHelper.baseApi.getPeerMessage(id.toString(), itemId.toString())
            .bindToLifecycle()
            .applySchedulers()
            .subscribe({
                if (checkIsLogin(it)) {
                    if (!it.isSuccessful) {
                        throw RuntimeException("${it.code()}:${it.message()}\n${it.errorBody()?.string()}")
                    }
                }
                it.body()?.let {
                    list.clear()
                    val sortedList = it.sortedBy {
                        it.timestamp
                    }
                    list.addAll(sortedList)
                    adapter.notifyDataSetChanged()
                    *//*if (page == 1) {
                        recycle.scrollToPosition(adapter.itemCount - 1)
                    }*//*
                }
            }, {
                it.printStackTrace()
                showMessage(it.message ?: "")
            })*/
    }

    private fun getItem() {
        if (itemId > 0) {

        }
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
        if (emojiPopup.isShowing) {
            emojiPopup.dismiss()
        }
        super.onDestroy()
    }
}