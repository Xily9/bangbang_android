package com.autowheel.bangbang.ui.note.fragment

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.ui.note.activity.NoteSearchActivity
import com.autowheel.bangbang.utils.startActivity
import kotlinx.android.synthetic.main.fragment_note.*

/**
 * Created by Xily on 2020/4/1.
 */
class NoteFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_note
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {

    }

    override fun initToolbar() {
        toolbar_title.text = "笔记"
        iv_search.setOnClickListener {
            startActivity<NoteSearchActivity>()
        }
    }

    private fun loadData() {

    }

}