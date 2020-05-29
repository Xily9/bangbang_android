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
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.model.DataManager
import com.autowheel.bangbang.model.bean.MessageEventBean
import com.autowheel.bangbang.model.network.bean.GeneralResponseBean
import com.autowheel.bangbang.model.network.bean.MessageBean
import com.autowheel.bangbang.model.network.bean.ReceiveMessageBean
import com.autowheel.bangbang.ui.msg.activity.ChatActivity
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.debug
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeremyliao.liveeventbus.LiveEventBus
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport
import java.lang.reflect.Type


class MessageService : Service() {
    private val notificationManager: NotificationManager
        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var isChatActivity = false
    private var chatId = 0
    private var isStop = false
    private var observer: Observer<MessageEventBean>? = null
    private var socket: Socket? = null
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
        if (!isChatActivity || chatId != messageBean.from_user_id) {
            val callback = {
                val nickName: String = if (messageBean.from_user_id > 0)
                    messageBean.from_nickname
                else
                    "系统消息"
                showNotification(nickName, messageBean.content, messageBean.from_user_id)
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
        val map = hashMapOf<String, Any>()
        map["room"] = chatId
        map["text"] = message
        socket?.emit("getmsg", Gson().toJson(map))
    }

    private fun initWebSocket() {
        /*val logInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .retryOnConnectionFailure(false)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
        val opts = IO.Options()
        opts.callFactory = okHttpClient
        opts.webSocketFactory = okHttpClient*/
        socket = IO.socket(BASE_URL + "/chat")?.apply {
            io().on(Manager.EVENT_TRANSPORT) {
                val transport = it[0] as Transport
                transport.on(Transport.EVENT_REQUEST_HEADERS) {
                    val headers = it[0] as MutableMap<String, List<String>>
                    // modify request headers
                    headers.put("Cookie", listOf(UserUtil.token.split(";")[0] + ";"))
                }
            }
            on(Socket.EVENT_CONNECT) {
                debug("connect")
                emit("join")
            }.on("sendmsg") {
                debug(it[0] as String)
                val type: Type =
                    object : TypeToken<GeneralResponseBean<ReceiveMessageBean>>() {}.type
                val message =
                    Gson().fromJson<GeneralResponseBean<ReceiveMessageBean>>(it[0] as String, type)
                if (message.code == 0) {
                    val messageBean = MessageBean(
                        message.data.room,
                        message.data.user_nickname,
                        UserUtil.profile.uid,
                        (System.currentTimeMillis() / 1000).toInt(),
                        message.data.content
                    )
                    receiveMessage(messageBean)
                }
            }.on(Socket.EVENT_DISCONNECT) {
                debug("disconnect")
            }
            connect()
        }
    }

    override fun onDestroy() {
        socket?.disconnect()
        socket?.off()
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