package com.autowheel.bangbang.ui.note

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
 * Created by Xily on 2020/4/1.
 */
class NoteFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_note
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initToolbar()
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {

    }

    override fun initToolbar() {
        (requireActivity() as MainActivity).setSupportActionBar(toolbar)
        toolbar_title.text = "笔记"
    }

    private fun loadData() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_index_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> startActivity<SearchActivity>()
        }
        return super.onOptionsItemSelected(item)
    }
}