package com.autowheel.bangbang.ui.user

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.ui.MainActivity
import com.autowheel.bangbang.ui.index.SearchActivity
import com.autowheel.bangbang.utils.startActivity
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
    }

    override fun initToolbar() {
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        requireActivity().title = ""
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