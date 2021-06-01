package com.example.jetpackmvvm.network

import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class BaseNetworkApi {

    private val okHttpClient: OkHttpClient
        get() {
            var builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
            builder = setHttpClientBuilder(builder)
            return builder.build()
        }

    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    abstract fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder


    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }
}