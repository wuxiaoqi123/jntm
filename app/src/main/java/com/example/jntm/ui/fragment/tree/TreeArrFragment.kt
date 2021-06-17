package com.example.jntm.ui.fragment.tree

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.bindViewPager2
import com.example.jntm.app.ext.init
import com.example.jntm.app.ext.setUiTheme
import com.example.jntm.app.util.CacheUtil
import com.example.jntm.databinding.FragmentViewpagerBinding
import com.example.jntm.viewmodel.state.TreeViewModel
import kotlinx.android.synthetic.main.include_viewpager.*

class TreeArrFragment : BaseFragment<TreeViewModel, FragmentViewpagerBinding>() {

    var titleData = arrayListOf("广场", "每日一问", "体系", "导航")

    private var fragments: ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(PlazaFragment())
        fragments.add(PlazaFragment())
        fragments.add(PlazaFragment())
        fragments.add(PlazaFragment())
    }

    override fun layoutId() = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        appViewModel.appColor.value?.let { setUiTheme(it, viewpager_linear) }
        include_viewpager_toolbar.run {
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        if (CacheUtil.isLogin()) {

                        } else {
                            nav().navigateAction(R.id.action_to_loginFragment)
                        }
                    }
                }
                true
            }
        }
    }

    override fun lazyLoadData() {
        view_pager.init(this, fragments).offscreenPageLimit = fragments.size
        magic_indicator.bindViewPager2(view_pager, mStringList = titleData) {

        }
    }

    override fun createObserver() {
        appViewModel.appColor.observeInFragment(this, {
            setUiTheme(it, viewpager_linear)
        })
    }
}