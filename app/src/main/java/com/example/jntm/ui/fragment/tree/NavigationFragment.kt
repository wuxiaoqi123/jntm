package com.example.jntm.ui.fragment.tree

import android.os.Bundle
import com.example.jntm.R
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.databinding.IncludeListBinding
import com.example.jntm.viewmodel.state.TreeViewModel

class NavigationFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {

    override fun layoutId() = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?) {
    }
}