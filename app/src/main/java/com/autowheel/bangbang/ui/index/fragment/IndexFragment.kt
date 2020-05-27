package com.autowheel.bangbang.ui.index.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachBean
import com.autowheel.bangbang.ui.index.activity.DetailActivity
import com.autowheel.bangbang.ui.index.activity.HelpActivity
import com.autowheel.bangbang.ui.index.activity.SearchActivity
import com.autowheel.bangbang.ui.index.adapter.CoachAdapter
import com.autowheel.bangbang.ui.index.adapter.ImageAdapter
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastInfo
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_index.*
import kotlinx.android.synthetic.main.item_fragment_index_header.view.*

/**
 * Created by Xily on 2020/3/25.
 */
class IndexFragment : BaseFragment() {
    private var totalDataCount: Int = 0
    private var page = 1
    private val eachPage = 20
    private lateinit var headerView: View
    private lateinit var adapter: CoachAdapter
    private var list = arrayListOf<CoachBean>()
    override fun getLayoutId(): Int {
        return R.layout.fragment_index
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initToolbar()
        initRecyclerView()
        swipe_refresh_layout.setColorSchemeResources(R.color.blue)
        swipe_refresh_layout.setOnRefreshListener {
            page = 1
            //startActivity<ChatActivity>("id" to 1)
            loadData(true)
        }
        loadData(true)
    }


    private fun initRecyclerView() {
        rv_coach.layoutManager = LinearLayoutManager(requireContext())
        adapter = CoachAdapter(list)
        headerView =
            layoutInflater.inflate(R.layout.item_fragment_index_header, rv_coach, false)
        headerView.banner.apply {
            adapter = ImageAdapter(arrayListOf("", ""))
            adapter.setOnBannerListener { data, position ->
                startActivity<HelpActivity>()
            }
            indicator = CircleIndicator(requireContext())
            setBannerRound2(20f)
            start()
        }
        adapter.headerView = headerView
        adapter.setOnItemClickListener {
            startActivity<DetailActivity>("id" to list[it].help_id)
        }
        rv_coach.adapter = adapter
        rv_coach.enableLoadMore()
        rv_coach.loadingMoreListener = {
            if (totalDataCount > list.size) {
                page++
                loadData(false)
            }
        }
    }

    override fun initToolbar() {
        toolbar_title.text = "首页"
        iv_search.setOnClickListener {
            startActivity<SearchActivity>()
        }
    }

    private fun loadData(isRefreshing: Boolean = false) {
        launch(tryBlock = {
            if (isRefreshing) {
                swipe_refresh_layout.isRefreshing = true
            } else {
                rv_coach.showLoading()
            }
            val result = RetrofitHelper.getApiService().getCoaches(page, eachPage)
            if (result.code == 0) {
                totalDataCount = result.length
                if (totalDataCount == 0) {
                    toastInfo("空空如也!")
                } else {
                    if (isRefreshing) {
                        list.clear()
                    }
                    list.addAll(result.data)
                    adapter.notifyDataSetChanged()
                }
            }
            rv_coach.showComplete()
        }, catchBlock = {
            it.printStackTrace()
            rv_coach.showError()
        }, finallyBlock = {
            if (isRefreshing) {
                swipe_refresh_layout.isRefreshing = false
            }
        })
    }
}