package com.example.jntm.ui.fragment.lookinfo

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
import com.example.jntm.app.weight.recyclerview.SpaceItemDecoration
import com.example.jntm.data.model.bean.CollectBus
import com.example.jntm.databinding.FragmentLookinfoBinding
import com.example.jntm.ui.adapter.AriticleAdapter
import com.example.jntm.viewmodel.request.RequestCollectViewModel
import com.example.jntm.viewmodel.request.RequestsLookInfoViewModel
import com.example.jntm.viewmodel.state.LookInfoViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_lookinfo.*
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*

class LookInfoFragment : BaseFragment<LookInfoViewModel, FragmentLookinfoBinding>() {

    private var shareId = 0

    private lateinit var loadsir: LoadService<Any>

    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    private val requestLookInfoViewModel: RequestsLookInfoViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_lookinfo

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            shareId = it.getInt("id")
        }
        mDatabind.vm = mViewModel
        appViewModel.appColor.value?.let {
            share_layout.setBackgroundColor(it)
        }
        toolbar.initClose("????????????") {
            nav().navigateUp()
        }
        loadsir = loadServiceInit(share_linear) {
            loadsir.showLoading()
            requestLookInfoViewModel.getLookinfo(shareId, true)
        }
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestLookInfoViewModel.getLookinfo(shareId, false)
            })
            it.initFloatBtn(floatbtn)
        }
        swipeRefresh.init {
            requestLookInfoViewModel.getLookinfo(shareId, true)
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
                    putParcelable(
                        "ariticleData",
                        articleAdapter.data[position - this@LookInfoFragment.recyclerView.headerCount]
                    )
                })
            }
        }
    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestLookInfoViewModel.getLookinfo(shareId, true)
    }

    override fun createObserver() {
        requestLookInfoViewModel.shareResponse.observe(viewLifecycleOwner, {
            mViewModel.name.set(it.coinInfo.username)
            mViewModel.info.set("?????? : ${it.coinInfo.coinCount}????????? : ${it.coinInfo.rank}")
        })
        requestLookInfoViewModel.shareListDataUistate.observe(viewLifecycleOwner, Observer {
            //?????? ???????????????????????????????????????????????????????????????
            loadListData(it, articleAdapter, loadsir, recyclerView, swipeRefresh)
        })
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
            userInfo.observeInFragment(this@LookInfoFragment) {
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
            }
            //??????????????????????????? ?????????Id?????????????????????id?????????????????????
            eventViewModel.collectEvent.observeInFragment(this@LookInfoFragment, Observer {
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