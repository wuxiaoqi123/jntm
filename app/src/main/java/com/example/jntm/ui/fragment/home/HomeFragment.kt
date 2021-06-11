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
            init("首页")
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
     * 懒加载
     */
    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        //请求轮播图数据
        requestHomeViewModel.getBannerData()
        //请求文章列表数据
        requestHomeViewModel.getHomeData(true)
    }

    override fun createObserver() {
        requestHomeViewModel.run {
            //监听首页文章列表请求的数据变化
            homeDataState.observe(viewLifecycleOwner, Observer {
                //设值 新写了个拓展函数，搞死了这个恶心的重复代码
                loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
            })
            //监听轮播图请求的数据变化
            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
                parseState(resultState, { data ->
                    //请求轮播图数据成功，添加轮播图到headview ，如果等于0说明没有添加过头部，添加一个
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
                //收藏或取消收藏操作成功，发送全局收藏消息
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
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
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
            //监听全局的主题颜色改变
            appColor.observeInFragment(this@HomeFragment) {
                setUiTheme(it, toolbar, floatbtn, swipeRefresh, loadsir, footView)
            }
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@HomeFragment) {
                articleAdapter.setAdapterAnimation(it)
            }
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
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