package com.example.jntm.ui.fragment.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.example.jntm.R
import com.example.jntm.app.base.BaseFragment
import com.example.jntm.app.ext.showMessage
import com.example.jntm.databinding.FragmentLoginBinding
import com.example.jntm.viewmodel.request.RequestLoginRegisterViewModel
import com.example.jntm.viewmodel.state.LoginRegisterViewModel

class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun layoutId(): Int = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
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

        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }
    }
}