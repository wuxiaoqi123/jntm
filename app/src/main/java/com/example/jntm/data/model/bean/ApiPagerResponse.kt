package com.example.jntm.data.model.bean

import java.io.Serializable

data class ApiPagerResponse<T>(
    var datas: T,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
) : Serializable {

    fun isEmpty() = (datas as List<*>).size == 0

    fun isRefresh() = offset == 0

    fun hasMore() = !over
}