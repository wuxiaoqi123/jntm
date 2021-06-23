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
import com.example.jntm.databinding.IncludeListBinding
import com.example.jntm.ui.adapter.NavigationAdapter
import com.example.jntm.viewmodel.request.RequestTreeViewModel
import com.example.jntm.viewmodel.state.TreeViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

class NavigationFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {

    private lateinit var loadsir: LoadService<Any>

    override fun layoutId() = R.layout.include_list

    private val navigationAdapter: NavigationAdapter by lazy { NavigationAdapter(arrayListOf()) }

    private val requestTreeViewModel: RequestTreeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        loadsir = loadServiceInit(swipeRefresh) {
            loadsir.showLoading()
            requestTreeViewModel.getNavigationData()
        }
        recyclerView.init(LinearLayoutManager(context), navigationAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(floatbtn)
        }
        swipeRefresh.init {
            requestTreeViewModel.getNavigationData()
        }
        navigationAdapter.setNavigationAction { item, view ->
            nav().navigateAction(R.id.action_to_webFragment,
                Bundle().apply {
                    putParcelable("ariticleData", item)
                }
            )
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestTreeViewModel.getNavigationData()
    }

    override fun createObserver() {
        requestTreeViewModel.navigationDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            if (it.isSuccess) {
                loadsir.showSuccess()
                navigationAdapter.setList(it.listData)
            } else {
                loadsir.showError(it.errMessage)
            }
        })
        appViewModel.run {
            //监听全局的主题颜色改变
            appColor.observeInFragment(this@NavigationFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir)
            })
            //监听全局的列表动画改编
            appAnimation.observeInFragment(this@NavigationFragment, Observer {
                navigationAdapter.setAdapterAnimation(it)
            })
        }
    }
}