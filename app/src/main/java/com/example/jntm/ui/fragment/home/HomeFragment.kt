package com.example.jntm.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.jntm.R
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.weight.recyclerview.DefineLoadMoreView
import com.example.jntm.databinding.FragmentHomeBinding
import com.example.jntm.ui.adapter.AriticleAdapter
import com.example.jntm.viewmodel.request.RequestCollectViewModel
import com.example.jntm.viewmodel.state.HomeViewModel
import com.kingja.loadsir.core.LoadService

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    private lateinit var loadsir: LoadService<Any>

    private lateinit var footView: DefineLoadMoreView

    private val requestCollectViewModel: RequestCollectViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
    }
}