package com.example.jntm.data.repository.request

import com.example.jetpackmvvm.network.AppException
import com.example.jntm.app.network.apiService
import com.example.jntm.app.util.CacheUtil
import com.example.jntm.data.model.bean.ApiPagerResponse
import com.example.jntm.data.model.bean.ApiResponse
import com.example.jntm.data.model.bean.AriticleResponse
import com.example.jntm.data.model.bean.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

val HttpRequestCoroutine: HttpReqeustManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpReqeustManager()
}

class HttpReqeustManager {

    suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return withContext(Dispatchers.IO) {
            val listData = async {
                apiService.getArticleList(pageNo)
            }
            if (CacheUtil.isNeedTop() && pageNo == 0) {
                val topData = async { apiService.getTopArticleList() }
                listData.await().data.datas.addAll(0, topData.await().data)
                listData.await()
            } else {
                listData.await()
            }
        }
    }

    suspend fun register(username: String, password: String): ApiResponse<UserInfo> {
        val registerData = apiService.register(username, password, password)
        if (registerData.isSucces()) {
            return apiService.login(username, password)
        } else {
            throw AppException(registerData.errorCode, registerData.errorMsg)
        }
    }

    suspend fun getProjectData(
        pageNo: Int,
        cid: Int = 0,
        isNew: Boolean = false
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return if (isNew) {
            apiService.getProjectNewData(pageNo)
        } else {
            apiService.getProjectDataByType(pageNo, cid)
        }
    }
}