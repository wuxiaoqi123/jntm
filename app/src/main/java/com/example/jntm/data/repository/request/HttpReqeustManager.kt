package com.example.jntm.data.repository.request

import com.example.jetpackmvvm.network.AppException
import com.example.jntm.app.network.apiService
import com.example.jntm.data.model.bean.ApiResponse
import com.example.jntm.data.model.bean.UserInfo

val HttpReqeustCoroutine: HttpReqeustManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpReqeustManager()
}

class HttpReqeustManager {

    suspend fun register(username: String, password: String): ApiResponse<UserInfo> {
        val registerData = apiService.register(username, password, password)
        if (registerData.isSucces()) {
            return apiService.login(username, password)
        } else {
            throw AppException(registerData.errorCode, registerData.errorMsg)
        }
    }
}