package com.autowheel.bangbang.ui.user.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastWarning
import kotlinx.android.synthetic.main.activity_settings.*


/**
 * Created by Xily on 2020/4/28.
 */
class SettingsActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "设置"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun initViews(savedInstanceState: Bundle?) {
        layout_safe_center.setOnClickListener {
            startActivity<SafeCenterActivity>()
        }
        layout_evaluate.setOnClickListener {
            try {
                val uri: Uri = Uri.parse("market://details?id=$packageName")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                toastWarning("您的手机没有安装Android应用市场")
            }
        }
    }
}