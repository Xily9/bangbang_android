package com.autowheel.bangbang.model.pref

import android.content.SharedPreferences
import android.util.Base64
import java.io.*
import kotlin.reflect.KProperty

/**
 * Created by Xily on 2019/3/11.
 */
abstract class PrefHelper<T>(private val default: T) {

    abstract fun getSharedPreferences(): SharedPreferences

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val name = property.name
        with(getSharedPreferences()) {
            return when (default) {
                is String -> getString(name, default)
                is Int -> getInt(name, default)
                is Boolean -> getBoolean(name, default)
                is Float -> getFloat(name, default)
                is Long -> getLong(name, default)
                is Serializable -> strToObj(getString(name, ""))
                    ?: default
                else -> getString(name, null)
            } as T
        }
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val name = property.name
        with(getSharedPreferences().edit()) {
            when (value) {
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                is Long -> putLong(name, value)
                is Serializable -> putString(name, objToStr(value))
                else -> putString(name, null)
            }
            apply()
        }
    }

    private fun objToStr(obj: Any?): String? {
        //创建字节输出流
        val baos = ByteArrayOutputStream()
        //创建字节对象输出流
        var out: ObjectOutputStream? = null
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = ObjectOutputStream(baos)
            out.writeObject(obj)
            return String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
        } catch (e: Exception) {
            //e.printStackTrace()
        } finally {
            try {
                baos.close()
                out?.close()
            } catch (e: Exception) {
                //e.printStackTrace()
            }
        }
        return null
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> strToObj(string: String?): T? {
        val buffer = Base64.decode(string, Base64.DEFAULT)
        //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
        val bais = ByteArrayInputStream(buffer)
        var ois: ObjectInputStream? = null
        try {
            ois = ObjectInputStream(bais)
            return ois.readObject() as T
        } catch (e: Exception) {
            //e.printStackTrace()
        } finally {
            try {
                bais.close()
                ois?.close()
            } catch (e: Exception) {
                //e.printStackTrace()
            }
        }
        return null
    }
}