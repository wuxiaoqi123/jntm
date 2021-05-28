package com.example.jntm.app.network

import com.example.jetpackmvvm.network.BaseResponse

data class ApiResponse<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
) : BaseResponse<T>() {

    override fun isSucces() = errorCode == 0

    override fun getResponseCode() = errorCode

    override fun getResponseData() = data

    override fun getResponseMsg() = errorMsg
}