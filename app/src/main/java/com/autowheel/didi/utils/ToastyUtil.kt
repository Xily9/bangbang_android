package com.autowheel.didi.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.autowheel.didi.MyApplication
import es.dmoral.toasty.Toasty

/**
 * Created by Xily on 2019/4/11.
 */
fun toastSuccess(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.success(MyApplication.getInstance(), message).show()
    }
}

fun toastInfo(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.info(MyApplication.getInstance(), message).show()
    }
}

fun toastWarning(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.warning(MyApplication.getInstance(), message).show()
    }
}

fun toastError(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.error(MyApplication.getInstance(), message).show()
    }
}