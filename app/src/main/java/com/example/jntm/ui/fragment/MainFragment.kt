package com.example.jntm.ui.fragment

import android.os.Bundle
import com.example.jntm.R
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.databinding.FragmentMainBinding
import com.example.jntm.viewmodel.state.MainViewModel

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun layoutId() = R.layout.fragment_main

    override fun initView(savedInstanceState: Bundle?) {
    }
}