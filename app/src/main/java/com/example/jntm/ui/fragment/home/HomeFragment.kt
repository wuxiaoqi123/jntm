package com.example.jntm.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.jetpackmvvm.ext.parseState
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.eventViewModel
import com.example.jntm.app.ext.*
import com.example.jntm.app.weight.banner.HomeBannerAdapter
import com.example.jntm.app.weight.banner.HomeBannerViewHolder
import com.example.jntm.app.weight.recyclerview.DefineLoadMoreView
import com.example.jntm.app.weight.recyclerview.SpaceItemDecoration
import com.example.jntm.data.model.bean.BannerResponse
import com.example.jntm.data.model.bean.CollectBus
import com.example.jntm.databinding.FragmentHomeBinding
import com.example.jntm.ui.adapter.AriticleAdapter
import com.example.jntm.viewmodel.request.RequestCollectViewModel
import com.example.jntm.viewmodel.request.RequestHomeViewModel
import com.example.jntm.viewmodel.state.HomeViewModel
import com.kingja.loadsir.core.LoadService
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    private lateinit var loadsir: LoadService<Any>

    private lateinit var footView: DefineLoadMoreView

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    private val requestHomeViewModel: RequestHomeViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        loadsir = loadServiceInit(swipeRefresh) {
            loadsir.showLoading()
            requestHomeViewModel.getBannerData()
            requestHomeViewModel.getHomeData(true)
        }
        toolbar.run {
            init("??????")
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        //TODO
                    }
                }
                true
            }
        }
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(8, ConvertUtils.dp2px(8f), false))
            footView = it.initFooter({
                requestHomeViewModel.getHomeData(false)
            })
            it.initFloatBtn(floatbtn)
        }
        swipeRefresh.init {
            requestHomeViewModel.getHomeData(true)
        }
        articleAdapter.run {
            setCollectClick { item, v, _ ->
                if (v.isChecked) {
                    requestCollectViewModel.uncollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }
            }
            setOnItemClickListener { adapter, view, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable(
                        "ariticleData",
                        articleAdapter.data[position - this@HomeFragment.recyclerView.headerCount]
                    )
                })
            }
            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_mainfragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt(
                                    "id",
                                    articleAdapter.data[position - this@HomeFragment.recyclerView.headerCount].userId
                                )
                            })
                    }
                }
            }
        }
    }

    /**
     * ?????????
     */
    override fun lazyLoadData() {
        //???????????? ?????????
        loadsir.showLoading()
        //?????????????????????
        requestHomeViewModel.getBannerData()
        //????????????????????????
        requestHomeViewModel.getHomeData(true)
    }

    override fun createObserver() {
        requestHomeViewModel.run {
            //?????????????????????????????????????????????
            homeDataState.observe(viewLifecycleOwner, Observer {
                //?????? ???????????????????????????????????????????????????????????????
                loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
            })
            //????????????????????????????????????
            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
                parseState(resultState, { data ->
                    //????????????????????????????????????????????????headview ???????????????0??????????????????????????????????????????
                    if (recyclerView.headerCount == 0) {
                        val headview =
                            LayoutInflater.from(context).inflate(R.layout.include_banner, null)
                                .apply {
                                    findViewById<BannerViewPager<BannerResponse, HomeBannerViewHolder>>(
                                        R.id.banner_view
                                    ).apply {
                                        adapter = HomeBannerAdapter()
                                        setLifecycleRegistry(lifecycle)
                                        setOnPageClickListener {
                                            nav().navigateAction(
                                                R.id.action_to_webFragment,
                                                Bundle().apply {
                                                    putParcelable(
                                                        "bannerdata",
                                                        data[it]
                                                    )
                                                })
                                        }
                                        create(data)
                                    }
                                }
                        recyclerView.addHeaderView(headview)
                        recyclerView.scrollToPosition(0)
                    }
                })
            })
        }
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //????????????????????????????????????????????????????????????
                eventViewModel.collectEvent.value = CollectBus(it.id, it.collect)
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })
        appViewModel.run {
            //?????????????????????????????? ?????????(??????)????????????????????????????????????????????????(????????????)???????????????????????????????????????
            userInfo.observeInFragment(this@HomeFragment, Observer {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in articleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in articleAdapter.data) {
                        item.collect = false
                    }
                }
                articleAdapter.notifyDataSetChanged()
            })
            //?????????????????????????????????
            appColor.observeInFragment(this@HomeFragment) {
                setUiTheme(it, toolbar, floatbtn, swipeRefresh, loadsir, footView)
            }
            //?????????????????????????????????
            appAnimation.observeInFragment(this@HomeFragment) {
                articleAdapter.setAdapterAnimation(it)
            }
            //??????????????????????????? ?????????Id?????????????????????id?????????????????????
            eventViewModel.collectEvent.observeInFragment(this@HomeFragment) {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        }
    }
}