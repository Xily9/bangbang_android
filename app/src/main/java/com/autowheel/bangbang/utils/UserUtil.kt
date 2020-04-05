package com.autowheel.bangbang.utils

import android.graphics.drawable.Drawable
import com.autowheel.bangbang.model.DataManager

/**
 * Created by Xily on 2020/4/6.
 */
object UserUtil {
    var avatarDrawable: Drawable? = null
    val isLogin
        get() = DataManager.token.isNotEmpty()
    var token = DataManager.token
        set(value) {
            DataManager.token = value
            field = value
        }
    var profile = DataManager.profile
        set(value) {
            DataManager.profile = profile
            field = value
        }

}