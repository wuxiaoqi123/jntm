package com.example.jntm.app.network

import com.example.jntm.app.util.CacheUtil
import okhttp3.Interceptor
import okhttp3.Response

class MyHeadInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("token", "token123456").build()
        builder.addHeader("device", "Android").build()
        builder.addHeader("isLogin", CacheUtil.isLogin().toString()).build()
        return chain.proceed(builder.build())
    }
}