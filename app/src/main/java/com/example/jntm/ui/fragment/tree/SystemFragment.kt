package com.example.jntm.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.*
import com.example.jntm.app.weight.recyclerview.SpaceItemDecoration
import com.example.jntm.data.model.bean.SystemResponse
import com.example.jntm.databinding.IncludeListBinding
import com.example.jntm.ui.adapter.SystemAdapter
import com.example.jntm.viewmodel.request.RequestTreeViewModel
import com.example.jntm.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

class SystemFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {

    private lateinit var loadsir: LoadService<Any>

    override fun layoutId() = R.layout.include_list

    private val systemAdapter: SystemAdapter by lazy { SystemAdapter(arrayListOf()) }

    private val requestTreeViewModel: RequestTreeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestTreeViewModel.getSystemData()
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), systemAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestTreeViewModel.getSystemData()
        }
        systemAdapter.run {
            setOnItemClickListener { _, view, position ->
                if (systemAdapter.data[position].children.isNotEmpty()) {
                    nav().navigateAction(R.id.action_mainfragment_to_systemArrFragment,
                        Bundle().apply {
                            putParcelable("data", systemAdapter.data[position])
                        }
                    )
                }
            }
            setChildClick { item: SystemResponse, view, position ->
                nav().navigateAction(R.id.action_mainfragment_to_systemArrFragment,
                    Bundle().apply {
                        putParcelable("data", item)
                        putInt("index", position)
                    }
                )
            }
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestTreeViewModel.getSystemData()
    }

    override fun createObserver() {
        requestTreeViewModel.systemDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            if (it.isSuccess) {
                loadsir.showSuccess()
                systemAdapter.setList(it.listData)
            } else {
                loadsir.showError(it.errMessage)
            }
        })

        appViewModel.run {
            //监听全局的主题颜色改变
            appColor.observeInFragment(this@SystemFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir)
            })
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@SystemFragment, Observer {
                systemAdapter.setAdapterAnimation(it)
            })
        }

    }
}