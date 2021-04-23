package com.example.jntm.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.jetpackmvvm.base.fragment.BaseVmDbFragment
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jntm.app.ext.dismissLoadingExt
import com.example.jntm.app.ext.hideSoftKeyboard
import com.example.jntm.app.ext.showLoadingExt

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>() {

    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun lazyLoadData() {
    }

    override fun createObserver() {
    }

    override fun initData() {
    }

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }

    override fun lazyLoadTime(): Long = 300
}