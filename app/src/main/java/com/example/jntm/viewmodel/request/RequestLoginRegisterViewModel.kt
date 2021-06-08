package com.example.jntm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.jetpackmvvm.state.ResultState
import com.example.jntm.app.network.apiService
import com.example.jntm.data.model.bean.UserInfo
import com.example.jntm.data.repository.request.HttpRequestCoroutine

class RequestLoginRegisterViewModel : BaseViewModel() {

    var loginResult = MutableLiveData<ResultState<UserInfo>>()

    fun loginReq(username: String, password: String) {
        request(
            { apiService.login(username, password) },
            loginResult,
            true,
            "存在登录中..."
        )
    }

    fun registerAndlogin(username: String, password: String) {
        request({
            HttpRequestCoroutine.register(username, password)
        }, loginResult, true, "正在注册中...")
    }
}