package com.autowheel.bangbang.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewConfiguration
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.autowheel.bangbang.MyApplication


private val outMetrics by lazy {
    val displayMetrics = DisplayMetrics()
    val windowManager = MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getRealMetrics(displayMetrics)
    displayMetrics
}

/**
 * 获取设备宽度
 *
 * @return
 */
val deviceWidth: Int
    get() = outMetrics.widthPixels

/**
 * 获取设备高度
 *
 * @return
 */
val deviceHeight: Int
    get() = outMetrics.heightPixels

//获取dpi
val deviceDpi = outMetrics.densityDpi


val cacheDir: String
    get() = MyApplication.getInstance().externalCacheDir!!.path

val splashCacheDir: String
    get() = MyApplication.getInstance().getExternalFilesDir("splash")!!.path

//状态栏高度
val statusBarHeight: Int
    get() = MyApplication.getInstance().resources.run {
        val id = getIdentifier("status_bar_height", "dimen", "android")
        if (id > 0) getDimensionPixelSize(id) else 0
    }

//获取虚拟按键的高度
val navigationBarHeight: Int
    get() {
        var result = 0
        MyApplication.getInstance().apply {
            //全面屏手势
            if (Settings.Global.getInt(MyApplication.getInstance().contentResolver, "force_fsg_nav_bar", 0) != 0) {
                return 0
            }
            if (hasNavBar()) {
                val res = resources
                val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId)
                }
            }
        }
        return result
    }

/**
 * dp转px
 *
 * @param dpValue dp值
 * @return
 */
fun dp2px(dpValue: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, outMetrics).toInt()
}

fun Activity.setStatusBarUpper() {
    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    window.statusBarColor = Color.TRANSPARENT
}

fun Activity.hideSoftInput() {
    val imm = MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun showSoftInput(view: View) {
    val imm = MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 显示或隐藏StatusBar
 *
 * @param enable false 显示，true 隐藏
 */
fun hideStatusBar(window: Window, enable: Boolean) {
    val p = window.attributes
    if (enable) {
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
    } else {
        p.flags = p.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
    }
    window.attributes = p
}


fun Context.hasNavBar(): Boolean {
    val res = resources
    val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
    return if (resourceId != 0) {
        var hasNav = res.getBoolean(resourceId)
        // check override flag
        val sNavBarOverride = getNavBarOverride()
        if ("1" == sNavBarOverride) {
            hasNav = false
        } else if ("0" == sNavBarOverride) {
            hasNav = true
        }
        hasNav
    } else { // fallback
        !ViewConfiguration.get(this).hasPermanentMenuKey()
    }
}

/**
 * 判断虚拟按键栏是否重写
 *
 * @return
 */
private fun getNavBarOverride(): String? {
    var sNavBarOverride: String? = null
    try {
        val c = Class.forName("android.os.SystemProperties")
        val m = c.getDeclaredMethod("get", String::class.java)
        m.isAccessible = true
        sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
    } catch (e: Throwable) {
    }

    return sNavBarOverride
}

fun Activity.setDarkStatusIcon(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        var vis = decorView.systemUiVisibility
        vis = if (dark) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#33000000")
    }
}

fun Fragment.setDarkStatusIcon(dark: Boolean) {
    requireActivity().run {
        setDarkStatusIcon(dark)
    }
}