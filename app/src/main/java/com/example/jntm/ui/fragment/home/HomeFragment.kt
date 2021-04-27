package com.example.jntm.ui.fragment.home

import android.os.Bundle
import com.example.jntm.R
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.databinding.FragmentHomeBinding
import com.example.jntm.viewmodel.state.HomeViewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
    }
}