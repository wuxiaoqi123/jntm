package com.example.jntm.ui.fragment.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.jetpackmvvm.ext.parseState
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.*
import com.example.jntm.app.weight.loadCallBack.ErrorCallback
import com.example.jntm.databinding.FragmentViewpagerBinding
import com.example.jntm.viewmodel.request.RequestProjectViewModel
import com.example.jntm.viewmodel.state.ProjectViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_viewpager.*

class ProjectFragment : BaseFragment<ProjectViewModel, FragmentViewpagerBinding>() {

    private lateinit var loadsir: LoadService<Any>

    var fragments: ArrayList<Fragment> = arrayListOf()

    var mDataList: ArrayList<String> = arrayListOf()

    private val requestProjectViewModel: RequestProjectViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        loadsir = loadServiceInit(view_pager) {
            loadsir.showLoading()
            requestProjectViewModel.getProjectTitleData()
        }
        view_pager.init(this, fragments)
        magic_indicator.bindViewPager2(view_pager, mDataList)
        appViewModel.appColor.value?.let {
            setUiTheme(it, viewpager_linear, loadsir)
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        //请求标题数据
        requestProjectViewModel.getProjectTitleData()
    }

    override fun createObserver() {
        requestProjectViewModel.titleData.observe(viewLifecycleOwner, Observer { data ->
            parseState(data, {
                mDataList.clear()
                fragments.clear()
                mDataList.add("最新项目")
                mDataList.addAll(it.map { it.name })
                fragments.add(ProjectChildFragment.newInstance(0, true))
                it.forEach { classify ->
                    fragments.add(ProjectChildFragment.newInstance(classify.id, false))
                }
                magic_indicator.navigator.notifyDataSetChanged()
                view_pager.adapter?.notifyDataSetChanged()
                view_pager.offscreenPageLimit = fragments.size
                loadsir.showSuccess()
            }, {
                //请求项目标题失败
                loadsir.showCallback(ErrorCallback::class.java)
                loadsir.setErrorText(it.errorMsg)
            })
        })
        appViewModel.appColor.observeInFragment(this, Observer {
            setUiTheme(it, viewpager_linear, loadsir)
        })
    }
}