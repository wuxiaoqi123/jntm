package com.example.jntm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.jetpackmvvm.base.viewmodel.BaseViewModel
import com.example.jetpackmvvm.ext.request
import com.example.jntm.app.network.apiService
import com.example.jntm.app.network.statecallback.CollectUiState
import com.example.jntm.app.network.statecallback.ListDataUiState
import com.example.jntm.data.model.bean.CollectResponse
import com.example.jntm.data.model.bean.CollectUrlResponse

class RequestCollectViewModel : BaseViewModel() {

    private var pageNo = 0

    val collectUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    val collectUrlUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    var ariticleDataState: MutableLiveData<ListDataUiState<CollectResponse>> = MutableLiveData()

    var urlDataState: MutableLiveData<ListDataUiState<CollectUrlResponse>> = MutableLiveData()

    fun collect(id: Int) {
        request({ apiService.collect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = id)
            collectUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = id)
            collectUiState.value = uiState
        })
    }

    fun uncollect(id: Int) {
        request({ apiService.uncollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUiState.value = uiState
        }, {
            val uiState = CollectUiState(false, true, id, it.errorMsg)
            collectUiState.value = uiState
        })
    }

    fun collectUrl(name: String, link: String) {
        request({ apiService.collectUrl(name, link) }, {
            val uiState = CollectUiState(true, true, id = it.id)
            collectUrlUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = 0)
            collectUrlUiState.value = uiState
        })
    }

    fun uncollectUrl(id: Int) {
        request({ apiService.uncollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUrlUiState.value = uiState
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUrlUiState.value = uiState
        })
    }

    fun getCollectAriticleData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ apiService.getCollectData(pageNo) }, {
            pageNo++
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.isEmpty(),
                hasMore = it.hasMore(),
                isFirstEmpty = isRefresh && it.isEmpty(),
                listData = it.datas
            )
            ariticleDataState.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<CollectResponse>()
                )
            ariticleDataState.value = listDataUiState
        })
    }

    fun getCollectUrlData() {
        request({ apiService.getCollectUrlData() }, {
            //请求成功
            it.map {
                if (it.order == 0) {
                    it.order = 1
                }
            }
            val listDataUiState =
                ListDataUiState(
                    isRefresh = true,
                    isSuccess = true,
                    hasMore = false,
                    isEmpty = it.isEmpty(),
                    listData = it
                )
            urlDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<CollectUrlResponse>()
                )
            urlDataState.value = listDataUiState
        })
    }
}