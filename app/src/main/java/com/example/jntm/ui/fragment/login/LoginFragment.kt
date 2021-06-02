package com.example.jntm.ui.fragment.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.example.jetpackmvvm.ext.nav
import com.example.jetpackmvvm.ext.parseState
import com.example.jntm.R
import com.example.jntm.app.appViewModel
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.hideSoftKeyboard
import com.example.jntm.app.ext.initClose
import com.example.jntm.app.ext.showMessage
import com.example.jntm.app.util.CacheUtil
import com.example.jntm.app.util.SettingUtil
import com.example.jntm.databinding.FragmentLoginBinding
import com.example.jntm.viewmodel.request.RequestLoginRegisterViewModel
import com.example.jntm.viewmodel.state.LoginRegisterViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.include_toolbar.*

class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()
        toolbar.initClose("登录") {
            nav().navigateUp()
        }
        appViewModel.appColor.value?.let {
            SettingUtil.setShapColor(loginSub, it)
            loginGoregister.setTextColor(it)
            toolbar.setBackgroundColor(it)
        }
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.loginResult.observe(viewLifecycleOwner, { resultState ->
            parseState(resultState, {
                CacheUtil.setUser(it)
                CacheUtil.setIsLogin(true)
                appViewModel.userInfo.value = it
                nav().navigateUp()
            }, {
                showMessage(it.errorMsg)
            })
        })
    }

    inner class ProxyClick {

        fun clear() {
            mViewModel.username.set("")
        }

        fun login() {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                else -> requestLoginRegisterViewModel.loginReq(
                    mViewModel.username.get(),
                    mViewModel.password.get()
                )
            }
        }

        fun goRegister() {
            hideSoftKeyboard(activity)
//            nav().navigateAction()
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }
    }
}