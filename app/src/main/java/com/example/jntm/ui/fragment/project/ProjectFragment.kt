package com.example.jntm.ui.fragment.project

import android.os.Bundle
import com.example.jntm.R
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.databinding.FragmentViewpagerBinding
import com.example.jntm.viewmodel.state.ProjectViewModel

class ProjectFragment : BaseFragment<ProjectViewModel, FragmentViewpagerBinding>() {

    override fun layoutId() = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
    }
}