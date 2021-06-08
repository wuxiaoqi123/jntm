package com.example.jntm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.jetpackmvvm.state.ResultState
import com.example.jntm.app.network.apiService
import com.example.jntm.app.network.statecallback.ListDataUiState
import com.example.jntm.data.model.bean.AriticleResponse
import com.example.jntm.data.model.bean.BannerResponse
import com.example.jntm.data.repository.request.HttpRequestCoroutine

class RequestHomeViewModel : BaseViewModel() {

    var pageNo = 0

    var homeDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    var bannerData: MutableLiveData<ResultState<ArrayList<BannerResponse>>> = MutableLiveData()

    fun getHomeData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({
            HttpRequestCoroutine.getHomeData(pageNo)
        }, {
            pageNo++
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.isEmpty(),
                hasMore = it.hasMore(),
                isFirstEmpty = isRefresh && it.isEmpty(),
                listData = it.datas
            )
            homeDataState.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            homeDataState.value = listDataUiState
        })
    }

    fun getBannerData() {
        request({ apiService.getBanner() }, bannerData)
    }
}