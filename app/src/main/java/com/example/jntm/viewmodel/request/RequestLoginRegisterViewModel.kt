package com.example.jntm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.state.ResultState
import com.example.jntm.app.network.NetworkApi
import com.example.jntm.app.network.apiService
import com.example.jntm.data.model.bean.UserInfo

class RequestLoginRegisterViewModel : BaseViewModel() {

    var loginResult = MutableLiveData<ResultState<UserInfo>>()

    fun loginReq(username: String, password: String) {

    }

    fun registerAndlogin(username: String, password: String) {

    }
}