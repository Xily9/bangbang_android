package com.autowheel.bangbang.ui.index

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.utils.startActivity
import kotlinx.android.synthetic.main.fragment_index.*

/**
 * Created by Xily on 2020/3/25.
 */
class IndexFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_index
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
        toolbar_title.text = "首页"
        iv_search.setOnClickListener {
            startActivity<SearchActivity>()
        }
    }

    private fun loadData() {

    }
}