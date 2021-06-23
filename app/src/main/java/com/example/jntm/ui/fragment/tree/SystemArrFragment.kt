package com.example.jntm.ui.fragment.tree

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.jetpackmvvm.ext.nav
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.bindViewPager2
import com.example.jntm.app.ext.init
import com.example.jntm.app.ext.initClose
import com.example.jntm.data.model.bean.SystemResponse
import com.example.jntm.databinding.FragmentSystemBinding
import com.example.jntm.viewmodel.state.TreeViewModel
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.include_viewpager.*

class SystemArrFragment : BaseFragment<TreeViewModel, FragmentSystemBinding>() {

    lateinit var data: SystemResponse

    var index = 0

    private var fragments: ArrayList<Fragment> = arrayListOf()

    override fun layoutId() = R.layout.fragment_system

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            data = it.getParcelable("data")!!
            index = it.getInt("index")
        }
        toolbar.initClose(data.name) {
            nav().navigateUp()
        }
        //初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let { viewpager_linear.setBackgroundColor(it) }
        //设置栏目标题居左显示
        (magic_indicator.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.LEFT

    }

    override fun lazyLoadData() {
        data.children.forEach {
            fragments.add(SystemChildFragment.newInstance(it.id))
        }
        //初始化viewpager2
        view_pager.init(this, fragments)
        //初始化 magic_indicator
        magic_indicator.bindViewPager2(view_pager, data.children.map { it.name })

        view_pager.offscreenPageLimit = fragments.size

        view_pager.postDelayed({
            view_pager.currentItem = index
        }, 100)
    }

    override fun createObserver() {

    }
}