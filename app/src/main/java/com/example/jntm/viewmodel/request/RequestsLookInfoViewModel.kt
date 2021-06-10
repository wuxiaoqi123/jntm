package com.example.jntm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.jntm.app.network.apiService
import com.example.jntm.app.network.statecallback.ListDataUiState
import com.example.jntm.data.model.bean.AriticleResponse
import com.example.jntm.data.model.bean.ShareResponse

class RequestsLookInfoViewModel : BaseViewModel() {

    var pageNo = 1

    var shareListDataUistate = MutableLiveData<ListDataUiState<AriticleResponse>>()

    var shareResponse = MutableLiveData<ShareResponse>()

    fun getLookinfo(id: Int, isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({
            apiService.getShareByIdData(id, pageNo)
        }, {
            pageNo++
            shareResponse.value = it
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = it.shareArticles.isRefresh(),
                    isEmpty = it.shareArticles.isEmpty(),
                    hasMore = it.shareArticles.hasMore(),
                    isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                    listData = it.shareArticles.datas
                )
            shareListDataUistate.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            shareListDataUistate.value = listDataUiState
        })
    }
}