package com.example.jetpackmvvm.base.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.getVmClazz
import com.example.jetpackmvvm.network.manager.NetState
import com.example.jetpackmvvm.network.manager.NetworkStateManager

abstract class BaseVmFragment<VM : BaseViewModel> : Fragment() {

    private val handler = Handler()

    private var isFirst: Boolean = true

    lateinit var mViewModel: VM
    lateinit var mActivity: AppCompatActivity

    abstract fun layoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        registerDefUIChange()
        initData()
    }

    open fun onNetworkStateChanged(netState: NetState) {}

    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun lazyLoadData()

    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            handler.postDelayed({
                lazyLoadData()
                NetworkStateManager.instance.mNetworkStateCallback.observeInFragment(this, {
                    if (!isFirst) {
                        onNetworkStateChanged(it)
                    }
                })
                isFirst = false
            }, lazyLoadTime())
        }
    }

    open fun initData() {}

    abstract fun showLoading(message: String = "请求网络中...")

    abstract fun dismissLoading()

    private fun registerDefUIChange() {
        mViewModel.loadingChange.showDialog.observeInFragment(this, {
            showLoading(it)
        })
        mViewModel.loadingChange.dismissDialog.observeInFragment(this, {
            dismissLoading()
        })
    }

    protected fun addLoadingObserve(vararg viewModels: BaseViewModel) {
        viewModels.forEach { viewModel ->
            //显示弹窗
            viewModel.loadingChange.showDialog.observeInFragment(this, {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingChange.dismissDialog.observeInFragment(this, {
                dismissLoading()
            })
        }
    }

    open fun lazyLoadTime() = 300L
}