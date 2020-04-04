package com.autowheel.bangbang.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable

/**
 * Created by Xily on 2020/1/28.
 */
fun Intent.putExtras(vararg params: Pair<String, Any>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
            is Serializable -> putExtra(key, value)
        }
    }
    return this
}

class HelperFragment : Fragment() {
    private var requestCode: Int = 0
    private var intent: Intent? = null
    private var callback: ((Intent?) -> Unit)? = null
    fun init(
        intent: Intent,
        callback: ((Intent?) -> Unit)
    ) {
        this.requestCode = _requestCode
        this.intent = intent
        this.callback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                callback?.invoke(data)
            } else {
                callback?.invoke(null)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        private var _requestCode = 0
            get() = field++
    }
}

inline fun <reified T : Activity> Activity.startActivity(vararg paramMap: Pair<String, Any>) {
    val intent = Intent(this, T::class.java).putExtras(*paramMap)
    this.startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity(vararg paramMap: Pair<String, Any>) {
    val intent = Intent(context, T::class.java).putExtras(*paramMap)
    startActivity(intent)
}

inline fun <reified T : Activity> FragmentActivity.startActivityForResult(
    vararg paramMap: Pair<String, Any>,
    crossinline callback: ((Intent?) -> Unit)
) {
    val intent = Intent(this, T::class.java).putExtras(*paramMap)
    val fm = supportFragmentManager
    val fragment = HelperFragment()
    fragment.init(intent) {
        callback(it)
        fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
    fm.beginTransaction().add(fragment, HelperFragment::class.java.simpleName)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(
    vararg paramMap: Pair<String, Any>,
    crossinline callback: ((Intent?) -> Unit)
) {
    val intent = Intent(context, T::class.java).putExtras(*paramMap)
    val fm = fragmentManager!!
    val fragment = HelperFragment()
    fragment.init(intent) {
        callback(it)
        fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
    fm.beginTransaction().add(fragment, HelperFragment::class.java.simpleName)
}

inline fun <reified T : Service> Activity.startService() {
    val intent = Intent(this, T::class.java)
    startService(intent)
}
