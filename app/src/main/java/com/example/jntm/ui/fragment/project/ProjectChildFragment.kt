package com.example.jntm.ui.fragment.project

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
import com.example.jntm.app.eventViewModel
import com.example.jntm.app.ext.*
import com.example.jntm.app.weight.recyclerview.DefineLoadMoreView
import com.example.jntm.app.weight.recyclerview.SpaceItemDecoration
import com.example.jntm.data.model.bean.CollectBus
import com.example.jntm.databinding.IncludeListBinding
import com.example.jntm.ui.adapter.AriticleAdapter
import com.example.jntm.viewmodel.request.RequestCollectViewModel
import com.example.jntm.viewmodel.request.RequestProjectViewModel
import com.example.jntm.viewmodel.state.ProjectViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

class ProjectChildFragment : BaseFragment<ProjectViewModel, IncludeListBinding>() {

    companion object {
        fun newInstance(cid: Int, isNew: Boolean): ProjectChildFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            args.putBoolean("isNew", isNew)
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }

    private lateinit var loadsir: LoadService<Any>

    private lateinit var footView: DefineLoadMoreView

    private var isNew = false

    private var cid = 0

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    private val requestProjectViewModel: RequestProjectViewModel by viewModels()

    override fun layoutId() = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            isNew = it.getBoolean("isNew")
            cid = it.getInt("cid")
        }
        loadsir = loadServiceInit(swipeRefresh) {
            loadsir.showLoading()
            requestProjectViewModel.getProjectData(true, cid, isNew)
        }
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //?????????????????????????????????
                requestProjectViewModel.getProjectData(false, cid, isNew)
            })
            //?????????FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        swipeRefresh.init {
            requestProjectViewModel.getProjectData(true, cid, isNew)
        }
        articleAdapter.run {
            setCollectClick { item, v, position ->
                if (v.isChecked) {
                    requestCollectViewModel.uncollect(item.id)
                } else {
                    requestCollectViewModel.collect(item.id)
                }
            }
            setOnItemClickListener { adapter, view, position ->
                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
                    putParcelable("ariticleData", articleAdapter.data[position])
                })
            }
            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        nav().navigateAction(
                            R.id.action_mainfragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt("id", articleAdapter.data[position].userId)
                            })
                    }
                }
            }
        }
    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestProjectViewModel.getProjectData(true, cid, isNew)
    }

    override fun createObserver() {
        requestProjectViewModel.projectDataState.observe(viewLifecycleOwner, Observer {
            //?????? ???????????????????????????????????????????????????????????????
            loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
        })
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
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
            userInfo.observeInFragment(this@ProjectChildFragment, Observer {
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
            appColor.observeInFragment(this@ProjectChildFragment, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir, footView)
            })
            //?????????????????????????????????
            appAnimation.observeInFragment(this@ProjectChildFragment, Observer {
                articleAdapter.setAdapterAnimation(it)
            })
            //??????????????????????????? ?????????Id?????????????????????id?????????????????????
            eventViewModel.collectEvent.observeInFragment(this@ProjectChildFragment, Observer {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            })
        }
    }
}