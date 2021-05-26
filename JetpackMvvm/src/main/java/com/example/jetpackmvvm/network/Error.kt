package com.example.jetpackmvvm.network

enum class Error(private val code: Int, private val err: String) {

    UNKNOWN(1000, "请求失败，请稍后再试"),
    PARSE_ERROR(1001, "解析错误，请稍后再试"),
    NETWORK_ERROR(1002, "网络连接错误，请稍后再试"),
    SSL_ERROR(1004, "证书错误，请稍后再试"),
    TIMEOUT_ERROR(1006, "网络连接超时，请稍后重试");

    fun getValue(): String = err

    fun getKey(): Int = code
}