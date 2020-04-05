package com.autowheel.bangbang.ui.user

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.model.DataManager
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.ui.MainActivity
import com.autowheel.bangbang.ui.index.SearchActivity
import com.autowheel.bangbang.utils.startActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/3/25.
 */
class UserFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_user
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initToolbar()
        initData()
        getProfile()
        iv_avatar.setOnClickListener {
            startActivity<AvatarActivity>()
        }
        btn_logout.setOnClickListener {
            logout()
        }
        layout_user.setOnClickListener {
            startActivity<UserActivity>()
        }
    }

    private fun getProfile() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getProfile()
            if (result.code == 0) {
                val profile = result.data
                DataManager.profile = profile
                initData()
            }
        })
    }

    private fun initData() {
        val profile = DataManager.profile
        if (profile.uid != 0) {
            tv_nickname.text = profile.nickname
            tv_signature.text = profile.signature
            Glide.with(this).load("${BASE_URL}/user/avatar/${profile.uid}").into(iv_avatar)
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireActivity())
            .setTitle("退出登录")
            .setMessage("确认要退出登录吗?")
            .setPositiveButton("确认") { dialog, which ->
                DataManager.clearUserPref()
                startActivity<LoginActivity>()
                requireActivity().finish()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun initToolbar() {
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        toolbar_title.text = "我的"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_user_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> startActivity<SearchActivity>()
        }
        return super.onOptionsItemSelected(item)
    }
}