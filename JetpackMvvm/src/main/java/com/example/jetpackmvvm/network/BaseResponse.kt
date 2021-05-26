package com.example.jetpackmvvm.network

abstract class BaseResponse<out T> {

    abstract fun isSucces(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String
}