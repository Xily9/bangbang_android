package com.autowheel.didi.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import java.io.File
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern

/**
 * CoroutineUtil
 */
fun CoroutineScope.launch(tryBlock: suspend CoroutineScope.() -> Unit,
                       catchBlock: (suspend CoroutineScope.(Throwable) -> Unit) = {},
                       finallyBlock: (suspend CoroutineScope.() -> Unit) = {}) =
    launch(Dispatchers.IO) {
        try {
            tryBlock()
        } catch (e: CancellationException) {
        } catch (e: Exception) {
            catchBlock(e)
        } finally {
            finallyBlock()
        }
    }

suspend fun <T>withMain(block: suspend CoroutineScope.() -> T) {
    withContext(Dispatchers.Main, block)
}


/**
 * ColorUtil
 */
fun Context.getAttrColor(resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

fun Activity.getAttrColor(resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

fun Fragment.getAttrColor(resId: Int): Int {
    return activity?.getAttrColor(resId) ?: 0
}

/**
 * otherTools
 */
inline fun <reified T : Activity> Activity.startActivity() {
    val intent = Intent(this, T::class.java)
    this.startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity() {
    val intent = Intent(context, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivity(bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity(bundle: Bundle) {
    val intent = Intent(context, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivity(extras: Intent) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(extras)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity(extras: Intent) {
    val intent = Intent(context, T::class.java)
    intent.putExtras(extras)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int) {
    val intent = Intent(this, T::class.java)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int) {
    val intent = Intent(context, T::class.java)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int, bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, bundle: Bundle) {
    val intent = Intent(context, T::class.java)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Service> Activity.startService() {
    val intent = Intent(this, T::class.java)
    startService(intent)
}

inline fun checkTime(func: () -> Unit) {
    val startTime = System.currentTimeMillis()
    func()
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    debug(msg = "用时:$duration")
}

fun random(min: Int, max: Int): Int {
    val random = Random()
    return random.nextInt(max) % (max - min + 1) + min
}

fun getWeeks(startTime: Long, endTime: Long): Int {
    if (endTime < startTime) {
        return 1
    }
    val res = ((endTime - startTime) / (7 * 24 * 60 * 60 * 1000L) + 1).toInt()
    return if (res <= 0 || res >= 22) {
        1
    } else res
}

fun sha512(str: String): String {
    val messageDigest: MessageDigest
    var encodeStr = ""
    try {
        messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.update(str.toByteArray(charset("UTF-8")))
        encodeStr = byte2Hex(messageDigest.digest())
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return encodeStr
}


fun md5(string: String): String {
    val md5: MessageDigest
    var encodeStr = ""
    try {
        md5 = MessageDigest.getInstance("MD5")
        encodeStr = byte2Hex(md5.digest(string.toByteArray()))
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return encodeStr
}

fun unicode2CH(str: String): String {
    var res = str
    val s = ArrayList<String>()
    val zhengz = "\\\\u[0-9,a-fA-F]{4}"
    val p = Pattern.compile(zhengz)
    val m = p.matcher(res)
    while (m.find()) {
        s.add(m.group())
    }
    var i = 0
    val j = 2
    while (i < s.size) {
        val code = s[i].substring(j, j + 4)
        val ch = Integer.parseInt(code, 16).toChar()
        res = res.replace(s[i], ch.toString())
        i++
    }
    return res
}

/**
 * 　　* 将byte转为16进制
 * 　　* @param bytes
 * 　　* @return
 */
private fun byte2Hex(bytes: ByteArray): String {
    val stringBuffer = StringBuilder()
    var temp: String
    for (aByte in bytes) {
        temp = Integer.toHexString(aByte.toInt() and 0xFF)
        if (temp.length == 1) {
            //1得到一位的进行补0操作
            stringBuffer.append("0")
        }
        stringBuffer.append(temp)
    }
    return stringBuffer.toString()
}

fun String.isInt(): Boolean {
    val pattern = Pattern.compile("^[-+]?[\\d]*$")
    return pattern.matcher(this).matches()
}

//View
fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visibleOrGone() {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun View.isVisible() = visibility == View.VISIBLE

fun EditText.showKeyboard() {
    val inputManager =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, 0)
}

fun EditText.text() = text.toString()

fun Activity.showKeyboard() {
    window.setSoftInputMode(
        WindowManager.LayoutParams
            .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
}

fun ImageView.load(resId: Int) {
    Glide.with(this).load(resId).into(this)
}

fun ImageView.load(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.load(file: File) {
    Glide.with(this).load(file).into(this)
}

fun ImageView.load(drawable: Drawable) {
    Glide.with(this).load(drawable).into(this)
}

fun ImageView.fadeLoad(file: File) {
    Glide.with(this).load(file).transition(withCrossFade()).into(this)
}

fun ImageView.fadeLoad(url: String) {
    Glide.with(this).load(url).transition(withCrossFade()).into(this)
}
