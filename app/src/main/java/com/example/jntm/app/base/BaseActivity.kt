package com.example.jntm.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.jetpackmvvm.base.activity.BaseVmDbActivity
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jntm.app.ext.dismissLoadingExt
import com.example.jntm.app.ext.showLoadingExt

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    abstract override fun layoutId(): Int

    abstract override fun initView(saveInstanceState: Bundle?)

    override fun createObserver() {

    }

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }
}