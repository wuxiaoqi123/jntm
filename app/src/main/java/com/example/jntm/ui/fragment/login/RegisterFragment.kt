package com.example.jntm.ui.fragment.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.navigateAction
import com.example.jetpackmvvm.ext.parseState
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.initClose
import com.example.jntm.app.ext.showMessage
import com.example.jntm.app.util.CacheUtil
import com.example.jntm.app.util.SettingUtil
import com.example.jntm.databinding.FragmentRegisterBinding
import com.example.jntm.viewmodel.request.RequestLoginRegisterViewModel
import com.example.jntm.viewmodel.state.LoginRegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.include_toolbar.*

class RegisterFragment : BaseFragment<LoginRegisterViewModel, FragmentRegisterBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_register

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()
        toolbar.initClose("注册") {
            nav().navigateUp()
        }
        appViewModel.appColor.value?.let {
            SettingUtil.setShapColor(registerSub, it)
            toolbar.setBackgroundColor(it)
        }
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.loginResult.observe(
            viewLifecycleOwner, { resultState ->
                parseState(resultState, {
                    CacheUtil.setIsLogin(true)
                    CacheUtil.setUser(it)
                    appViewModel.userInfo.value = it
                    nav().navigateAction(R.id.action_registerFrgment_to_mainFragment)
                }, {
                    showMessage(it.errorMsg)
                })
            }
        )
    }

    inner class ProxyClick {

        fun clear() {
            mViewModel.username.set("")
        }

        fun register() {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                mViewModel.password2.get().isEmpty() -> showMessage("请填写确认密码")
                mViewModel.password.get().length < 6 -> showMessage("密码最少6位")
                mViewModel.password.get() != mViewModel.password2.get() -> showMessage("密码不一致")
                else -> requestLoginRegisterViewModel.registerAndlogin(
                    mViewModel.username.get(),
                    mViewModel.password.get()
                )
            }
        }

        var onCheckedChangeListener1 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd.set(isChecked)
        }
        var onCheckedChangeListener2 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd2.set(isChecked)
        }
    }
}