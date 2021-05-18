package com.example.jntm.ui.fragment

import android.os.Bundle
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.init
import com.example.jntm.app.ext.initMain
import com.example.jntm.app.ext.interceptLongClick
import com.example.jntm.databinding.FragmentMainBinding
import com.example.jntm.viewmodel.state.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun layoutId() = R.layout.fragment_main

    override fun initView(savedInstanceState: Bundle?) {
        mainViewPager.initMain(this)
        mainBottom.init {
            when (it) {
                R.id.menu_main -> mainViewPager.setCurrentItem(0, false)
                R.id.menu_project -> mainViewPager.setCurrentItem(1, false)
                R.id.menu_system -> mainViewPager.setCurrentItem(2, false)
                R.id.menu_public -> mainViewPager.setCurrentItem(3, false)
                R.id.menu_me -> mainViewPager.setCurrentItem(4, false)
            }
        }
        mainBottom.interceptLongClick(
            R.id.menu_main,
            R.id.menu_project,
            R.id.menu_system,
            R.id.menu_public,
            R.id.menu_me
        )
    }

    override fun createObserver() {
        appViewModel.appColor.observeInFragment(this, {
            //TODO
        })
    }
}