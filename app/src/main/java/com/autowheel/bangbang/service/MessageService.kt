package com.autowheel.bangbang.service

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.autowheel.bangbang.R
import com.autowheel.bangbang.model.DataManager
import com.autowheel.bangbang.model.bean.MessageEventBean
import com.autowheel.bangbang.model.network.bean.MessageBean
import com.autowheel.bangbang.ui.msg.ChatActivity
import com.jeremyliao.liveeventbus.LiveEventBus

class MessageService : Service() {
    private val notificationManager: NotificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var isChatActivity = false
    private var chatId = 0
    private var isStop = false
    private var observer: Observer<MessageEventBean>? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initWebSocket()
        initListener()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initListener() {
        observer = Observer<MessageEventBean> {
            when (it.type) {
                1 -> {
                    sendMessage(it.message)
                }
                2 -> {
                    isChatActivity = it.isChatActivity
                    chatId = it.chatId
                    notificationManager.cancel(chatId)
                }
                3 -> {
                    stopSelf()
                }
            }
        }
            .apply {
                LiveEventBus.get("messageEvent", MessageEventBean::class.java).observeForever(this)
            }
    }

    private fun receiveMessage(messageBean: MessageBean) {
        LiveEventBus.get("message", MessageBean::class.java).post(messageBean)
        if (!isChatActivity || chatId != messageBean.from) {
            val callback = {
                val nickName: String = if (messageBean.from > 0)
                    messageBean.fromNickname
                else
                    "系统消息"
                showNotification(nickName, messageBean.message, messageBean.from)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkNotificationChannel {
                    callback.invoke()
                }
            } else {
                callback.invoke()
            }
        }
    }

    private fun showNotification(title: String, message: String, uid: Int) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("id", uid)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, "chat")
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_add_white_24dp)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(uid, notification)
    }

    private fun sendMessage(message: String) {

    }

    private fun initWebSocket() {

    }

    override fun onDestroy() {
        isStop = true
        observer?.let {
            LiveEventBus.get("messageEvent", MessageEventBean::class.java).removeObserver(it)
        }
        super.onDestroy()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(callback: () -> Unit) {
        if (!DataManager.notificationChannel) {
            val channelId = "chat"
            val channelName = "聊天消息"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importance, callback)
        } else {
            callback.invoke()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        channelName: String,
        importance: Int,
        callback: () -> Unit
    ) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        DataManager.notificationChannel = true
        callback.invoke()
    }

}